package test.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.*;
import test.gamelogictesting.*;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	AdaptTest.class,
	BreakLanceTest.class,
	ChangeTournamentColourTest.class,
	ChargeTest.class,
	CounterChargeTest.class,
	DisgraceTest.class,
	DodgeTest.class,
	GameStateTest.class,
	IvanhoeTest.class,
	KnockdownTest.class,
	OutmaneuverTest.class,
	OutwitTest.class,
	RetreatTest.class,
	RiposteTest.class,
	TournamentStartTest.class,
	tournamentsTest.class,
	CardTest.class,
	DeckTest.class,
	EndTurnTest.class,
	NetworkTest.class,
	ParserTest.class,
	RulesEngineValueCardsTest.class,
	UpdateEngineTest.class
})

public class AllTests {
} 
