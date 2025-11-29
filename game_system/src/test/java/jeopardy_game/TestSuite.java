package jeopardy_game;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        GameTest.class,
        CategoryTest.class,
        QuestionTest.class,
        GameBoardTest.class,
        GameDataTest.class,
        ParsingTest.class,
        ScoringTest.class,
        ReportingTest.class
})
public class TestSuite {
}
