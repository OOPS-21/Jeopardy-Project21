package jeopardy_game;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        GamePlayTest.class,
        CategoryTest.class,
        QuestionTest.class,
        GameBoardTest.class,
        ParsingTest.class,
        ScoringTest.class,
        ReportingTest.class
})
public class TestSuite {
}
