package guitests;

import backend.resource.TurboIssue;
import javafx.scene.input.KeyCode;
import org.junit.Test;
import ui.UI;
import ui.issuepanel.IssuePanel;
import util.events.UILogicRefreshEvent;
import util.events.UpdateDummyRepoEvent;

import static org.junit.Assert.assertEquals;

public class IssuePanelTests extends UITest {

    private static final int EVENT_DELAY = 1500;

    @Test
    public void keepSelectionTest() {
        // checks to see if IssuePanel keeps the same issue selected even after
        // the list is updated
        IssuePanel issuePanel = find("#dummy/dummy_col0");
        click("#dummy/dummy_col0_filterTextField");
        type("sort");
        press(KeyCode.SHIFT).press(KeyCode.SEMICOLON).release(KeyCode.SEMICOLON).release(KeyCode.SHIFT);
        type("date");
        push(KeyCode.ENTER);
        push(KeyCode.SPACE).push(KeyCode.SPACE);
        push(KeyCode.DOWN).push(KeyCode.DOWN);
        sleep(EVENT_DELAY);
        assertEquals(3, issuePanel.getSelectedIssue().getId());
        TurboIssue turboIssue1 = issuePanel.getSelectedIssue();
        sleep(EVENT_DELAY);
        UI.events.triggerEvent(new UpdateDummyRepoEvent(
                UpdateDummyRepoEvent.UpdateType.UPDATE_ISSUE, "dummy/dummy", 3, "updated issue"));
        UI.events.triggerEvent(new UILogicRefreshEvent());
        sleep(EVENT_DELAY);
        TurboIssue turboIssue2 = issuePanel.getSelectedIssue();
        assertEquals(true, turboIssue1.equals(turboIssue2));
        sleep(5000);
    }

}
