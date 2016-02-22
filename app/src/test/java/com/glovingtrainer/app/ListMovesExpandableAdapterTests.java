package com.glovingtrainer.app;

import com.glovingtrainer.app.reader.MoveGroup;
import com.glovingtrainer.app.reader.SharedTestData;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest = Config.NONE)
public class ListMovesExpandableAdapterTests
{
    @Test
    public void testGroupGetters()
    {
        List<MoveGroup> moveGroups = SharedTestData.EXPECTED_GLOVING_MOVES;
        ListMovesExpandableAdapter adapter = new ListMovesExpandableAdapter(RuntimeEnvironment.application, moveGroups);
        Assert.assertEquals(moveGroups.size(), adapter.getGroupCount());

        int groupPos = 0;
        for (MoveGroup moveGroup : moveGroups)
        {
            Assert.assertEquals(moveGroup.moveType, ((MoveGroup) adapter.getGroup(groupPos)).moveType);
            Assert.assertEquals(moveGroup.moves.size(), adapter.getChildrenCount(groupPos));

            int childPos = 0;
            for (String move : moveGroup.moves)
            {
                Assert.assertEquals(move, adapter.getChild(groupPos, childPos++));
            }

            ++groupPos;
        }
    }
}
