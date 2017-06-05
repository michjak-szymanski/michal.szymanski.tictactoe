/*
 * The MIT License
 *
 * Copyright 2017 Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.michal.szymanski.tictactoe.newcore.play;

import pl.michal.szymanski.tictactoe.play.PlayExecutor;
import pl.michal.szymanski.tictactoe.play.PlaySettings;
import pl.michal.szymanski.tictactoe.play.PlayInfo;
import pl.michal.szymanski.tictactoe.play.Play;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import pl.michal.szymanski.tictactoe.model.Board;
import pl.michal.szymanski.tictactoe.model.Player;
import pl.michal.szymanski.tictactoe.model.Point;
import pl.michal.szymanski.tictactoe.transport.Participant;
import pl.michal.szymanski.tictactoe.transport.ProxyResponse;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class PlayExecutorTest {

    class PassiveParticipant implements Participant {

        private String name;
        private String id;

        public PassiveParticipant(String name) {
            this.name = name;
        }

        public PassiveParticipant() {

        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public void getMoveField(ProxyResponse<Point> proxy) {
            System.out.println("get field");
        }

        @Override
        public String getDisplayName() {
            return this.name;
        }

        @Override
        public void receiveBoard(Board board) {
            System.out.println("show board");
        }

        @Override
        public void onGameEnd(PlayInfo play, PlaySettings.PlaySettingsGetters settings) {
            System.out.println("game end");
        }

        @Override
        public void onTurnTimeout() {
            System.out.println("turn timeout");
        }

    }

    class ActiveParticipant extends PassiveParticipant {

        private Stack<Point<Integer>> moves = new Stack();

        public void setProgrammedMoves(Stack<Point<Integer>> programmedMoves) {
            this.moves = programmedMoves;
        }

        @Override
        public void getMoveField(ProxyResponse<Point> proxy) {
            super.getMoveField(proxy); //To change body of generated methods, choose Tools | Templates.
            try {
                proxy.setReal(moves.pop());
            } catch (EmptyStackException e) {

            }
        }

    }
    PassiveParticipant p1;
    PassiveParticipant p2;
    ActiveParticipant a1;
    ActiveParticipant a2;
    Play play;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        p1 = new PassiveParticipant("A");
        p2 = new PassiveParticipant("B");

        a1 = new ActiveParticipant();
        a2 = new ActiveParticipant();

        a1.setName("A");
        a2.setName("B");

        Stack<Point<Integer>> a1Moves = new Stack();
        a1Moves.push(new Point(0, 0));
        a1Moves.push(new Point(1, 1));
        a1Moves.push(new Point(2, 2));
        Stack<Point<Integer>> a2Moves = new Stack();
        a2Moves.push(new Point(0, 1));
        a2Moves.push(new Point(0, 2));
        a2Moves.push(new Point(1, 2));

        a1.setProgrammedMoves(a1Moves);
        a2.setProgrammedMoves(a2Moves);

        play = new Play();
        play.settings().moveLimit(250, TimeUnit.MILLISECONDS).timeout(1, TimeUnit.SECONDS);
    }

    /**
     * Test of execute method, of class PlayExecutor.
     */
    @Test
    public void testExecute() {
    }

    /**
     * Test of isDone method, of class PlayExecutor.
     */
    @Test(timeout = 10000)
    public void testIfGameLastSpecifiedTime() {
        play.join(p1);
        play.join(p2);
        new PlayExecutor(play).execute();
    }

    @Test(timeout = 2000)
    public void testIfAllParticipantsReceivedEXACTLYGetFieldRequests() {
        play.settings().moveLimit(250, TimeUnit.MILLISECONDS).timeout(1, TimeUnit.SECONDS);

        p1 = Mockito.mock(PassiveParticipant.class);
        p1.setName("A");
        p2 = Mockito.mock(PassiveParticipant.class);
        p2.setName("B");
        play.join(p1);
        play.join(p2);
        new PlayExecutor(play).execute();

        Mockito.verify(p1, Mockito.times(2)).getMoveField(Mockito.any());
        Mockito.verify(p2, Mockito.times(2)).getMoveField(Mockito.any());

    }

    @Test(timeout = 5000)
    public void testIfAllParticipantsGetFieldsRequestResponsesWereMarkedAtBoard() {
        play.settings().moveLimit(250, TimeUnit.MILLISECONDS).timeout(1, TimeUnit.SECONDS);
        play.join(a1);
        play.join(a2);
        new PlayExecutor(play).execute();

        assertEquals(4, play.getInfo().getBoard().getSelector().getAllFields().stream().filter(el -> el.getOwner().isPresent()).collect(Collectors.toList()).size());
    }

    @Test(timeout = 5000)
    public void testGameEndWhenPlayerHit3PointsInLine() {
        play.settings().moveLimit(250, TimeUnit.MILLISECONDS).timeout(2, TimeUnit.SECONDS);
        Stack<Point<Integer>> a1Moves = new Stack();
        a1Moves.push(new Point(0, 0));
        a1Moves.push(new Point(1, 1));
        a1Moves.push(new Point(2, 2));
        Stack<Point<Integer>> a2Moves = new Stack();
        a2Moves.push(new Point(0, 1));
        a2Moves.push(new Point(0, 2));
        a2Moves.push(new Point(1, 2));
        a2Moves.push(new Point(2, 1));

        a1.setProgrammedMoves(a1Moves);
        a2.setProgrammedMoves(a2Moves);

        play.join(a1);
        play.join(a2);
        new PlayExecutor(play).execute();

        int totalMoves = play.getInfo().getBoard().getSelector().getAllFields().stream().filter(el -> el.getOwner().isPresent()).collect(Collectors.toList()).size();
        assertTrue(totalMoves == 5 || totalMoves == 6);
    }

    @Test(timeout = 5000)
    public void testIsWinnerPresent() {
        play.settings().moveLimit(250, TimeUnit.MILLISECONDS).timeout(2, TimeUnit.SECONDS);
        Stack<Point<Integer>> a1Moves = new Stack();
        a1Moves.push(new Point(0, 0));
        a1Moves.push(new Point(1, 1));
        a1Moves.push(new Point(2, 2));
        Stack<Point<Integer>> a2Moves = new Stack();
        a2Moves.push(new Point(0, 1));
        a2Moves.push(new Point(0, 2));
        a2Moves.push(new Point(1, 2));
        a2Moves.push(new Point(2, 1));

        a1.setProgrammedMoves(a1Moves);
        a2.setProgrammedMoves(a2Moves);

        play.join(a1);
        play.join(a2);
        new PlayExecutor(play).execute();

        assertTrue(play.getInfo().getWinner().isPresent());
    }

    @Test(timeout = 5000)
    public void testIsWinnerMatchParticipant() {
        play.settings().moveLimit(250, TimeUnit.MILLISECONDS).timeout(2, TimeUnit.SECONDS);
        Stack<Point<Integer>> a1Moves = new Stack();
        a1Moves.push(new Point(0, 0));
        a1Moves.push(new Point(1, 1));
        a1Moves.push(new Point(2, 2));
        Stack<Point<Integer>> a2Moves = new Stack();
        a2Moves.push(new Point(0, 1));
        a2Moves.push(new Point(0, 2));
        a2Moves.push(new Point(1, 2));
        a2Moves.push(new Point(2, 1));

        a1.setProgrammedMoves(a1Moves);
        a2.setProgrammedMoves(a2Moves);

        play.join(a1);
        play.join(a2);
        new PlayExecutor(play).execute();

        assertTrue(play.getInfo().getWinner().isPresent());
        Player winner = (Player) play.getInfo().getWinner().get();
        assertEquals(a1, winner.getConnector().get());
    }

    @Test(timeout = 5000)
    public void testAllParticipantsWereNotifiedAboutGameEnd() {
        play.settings().moveLimit(250, TimeUnit.MILLISECONDS).timeout(1, TimeUnit.SECONDS);

        p1 = Mockito.mock(PassiveParticipant.class);
        p1.setName("A");
        p2 = Mockito.mock(PassiveParticipant.class);
        p2.setName("B");
        play.join(p1);
        play.join(p2);
        new PlayExecutor(play).execute();

        Mockito.verify(p1, Mockito.times(1)).onGameEnd(Mockito.any(), Mockito.any());
        Mockito.verify(p2, Mockito.times(1)).onGameEnd(Mockito.any(), Mockito.any());
    }

}
