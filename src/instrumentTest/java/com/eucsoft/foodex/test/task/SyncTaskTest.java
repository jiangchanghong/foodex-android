package com.eucsoft.foodex.test.task;

import android.test.AndroidTestCase;

import com.eucsoft.foodex.db.FoodDAO;
import com.eucsoft.foodex.db.model.FoodPair;
import com.eucsoft.foodex.task.SyncTask;
import com.eucsoft.foodex.task.OnOk;
import com.eucsoft.foodex.test.db.FoodPairTestHelper;

import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class SyncTaskTest extends AndroidTestCase {

    private FoodDAO foodDAO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        foodDAO = new FoodDAO(getContext());
        foodDAO.clearFoodPairs();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        foodDAO.close();
    }

    public void testNotChanged() throws Exception {
        List<FoodPair> foodPairs = createFoodPairs();

        foodDAO.insertFoodPairs(foodPairs);

        SyncTask syncTask = new SyncTask(foodPairs);
        syncTask.run();

        FoodPairTestHelper.checkListsEqual(foodDAO.getAllFoodPairs(), foodPairs);
    }

    public void testFoodPairUpdated() throws Exception {
        List<FoodPair> foodPairs = createFoodPairs();

        List<FoodPair> foodPairsInDB = createFoodPairs();
        foodPairsInDB.get(0).user.foodURL = "!BLAAAAA!!!!!!!!";
        foodPairsInDB.get(0).user.mapURL = "!BLAAAAA!!!!!!!!";

        foodDAO.insertFoodPairs(foodPairsInDB);

        SyncTask syncTask = new SyncTask(foodPairs);
        syncTask.run();

        FoodPairTestHelper.checkListsEqual(foodDAO.getAllFoodPairs(), foodPairs);
    }

    public void testFoodPairAdded() throws Exception {
        List<FoodPair> foodPairs = createFoodPairs();
        List<FoodPair> forDBList = createFoodPairs();
        forDBList.remove(0);

        foodDAO.insertFoodPairs(forDBList);

        SyncTask syncTask = new SyncTask(foodPairs);
        syncTask.run();

        FoodPairTestHelper.checkListsEqual(foodDAO.getAllFoodPairs(), foodPairs);
    }

    public void testFoodPairRemoved() throws Exception {
        List<FoodPair> foodPairs = createFoodPairs();
        List<FoodPair> forDBList = createFoodPairs();

        forDBList.add(FoodPairTestHelper.getRandomFoodPair());
        foodDAO.insertFoodPairs(forDBList);

        SyncTask syncTask = new SyncTask(foodPairs);
        syncTask.run();

        FoodPairTestHelper.checkListsEqual(foodDAO.getAllFoodPairs(), foodPairs);
    }

    public void testOkCallbackIsTriggeredWhenDBUpdated() throws Exception {
        List<FoodPair> foodPairs = new ArrayList<FoodPair>();

        foodPairs.add(FoodPairTestHelper.getRandomFoodPairNotPaired());

        OnOk okCallback = mock(OnOk.class);

        new SyncTask(foodPairs)
        .onOk(okCallback)
        .executeSync();

        verify(okCallback, times(1)).onOk(anyMap());
    }

    public void testOkCallbackIsNotTriggeredWhenDBNotUpdated() throws Exception {
        List<FoodPair> foodPairs = createFoodPairs();

        OnOk okCallback = mock(OnOk.class);

        new SyncTask(foodPairs)
        .onOk(okCallback)
        .executeSync();

        verify(okCallback, times(0)).onOk(anyMap());
    }

    private List<FoodPair> createFoodPairs() {
        FoodPair foodPair1 = new FoodPair();
        foodPair1.user.foodId = "ddddcwef3242f32f";
        foodPair1.user.foodURL = "http://api.foodex.com/food/dddd/ddddcwef3242f32f.jpg";
        foodPair1.user.foodDate = new Date(1383690800877l);
        foodPair1.user.mapURL = "http://api.foodex.com/map/eeee/eeeewef3242f32f.jpg";
        foodPair1.user.bonAppetit = 0;
        foodPair1.stranger.foodId = "abcwef3242f32f";
        foodPair1.stranger.foodURL = "http://api.foodex.com/food/abc/abcwef3242f32f.jpg";
        foodPair1.stranger.mapURL = "http://api.foodex.com/map/azca/azcacwef3242f32f.jpg";
        foodPair1.stranger.bonAppetit = 1;

        FoodPair foodPair2 = new FoodPair();
        foodPair2.user.foodId = "abcdw0ef3242f32f";
        foodPair2.user.foodURL = "http://api.foodex.com/food/abcd/abcdw0ef3242f32f.jpg";
        foodPair2.user.foodDate = new Date(1383670400877l);
        foodPair2.user.mapURL = "http://api.foodex.com/map/bcde/bcdecwef3242f32f.jpg'";
        foodPair2.user.bonAppetit = 1;
        foodPair2.stranger.foodId = "abcd3cwef3242f32f";
        foodPair2.stranger.foodURL = "http://api.foodex.com/food/abcd/abcd3cwef3242f32f.jpg";
        foodPair2.stranger.mapURL = "http://api.foodex.com/map/abcd/abcd5wef3242f32f.jpg";
        foodPair2.stranger.bonAppetit = 0;

        List<FoodPair> foodPairs = new ArrayList<FoodPair>();
        foodPairs.add(foodPair1);
        foodPairs.add(foodPair2);
        return foodPairs;
    }
}
