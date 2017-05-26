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
package pl.michal.szymanski.ticktacktoe.core.model;

import pl.michal.szymanski.tictactoe.core.BoardField;
import pl.michal.szymanski.tictactoe.core.Board;
import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.michal.szymanski.tictactoe.core.BoardFieldType;
import pl.michal.szymanski.tictactoe.core.Move;
import pl.michal.szymanski.tictactoe.core.Player;
import pl.michal.szymanski.tictactoe.core.Point;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class BoardSelectorTest {

    Board board;

    @Test
    public void testGetSizeX() {
    }

    @Before
    public void setUp() {
        board = new Board(3);
    }

    /**
     * Test of getSizeY method, of class Board.
     */
    @Test
    public void testGetSizeY() {
    }

    /**
     * Test of getBoard method, of class Board.
     */
    @Test
    public void testGetBoard() {
    }

    /**
     * Test of getRow method, of class Board.
     */
    @Test
    public void testGetRow() {
        BoardField[] row = board.getSelector().getRow(1);
        assertEquals(0, row[0].getX());
        assertEquals(1, row[0].getY());

        assertEquals(1, row[1].getX());
        assertEquals(1, row[1].getY());
    }

    /**
     * Test of getColumn method, of class Board.
     */
    @Test
    public void testGetColumn() {
        BoardField[] column = board.getSelector().getColumn(1);
        assertEquals(1, column[0].getX());
        assertEquals(0, column[0].getY());

        assertEquals(1, column[1].getX());
        assertEquals(1, column[1].getY());

    }

    @Test
    public void testGetSimplified() {
        Player a = new Player("A");
        Player b = new Player("B");
        a.setBoardFieldType(BoardFieldType.XMark);
        b.setBoardFieldType(BoardFieldType.OMark);

        board.doMove(new Move(a, new Point(0, 0)));
        board.doMove(new Move(a, new Point(1, 0)));
        board.doMove(new Move(a, new Point(2, 0)));

        board.doMove(new Move(b, new Point(2, 2)));
        board.doMove(new Move(b, new Point(1, 1)));

        String[][] result = board.getSelector().getSimplified();
        assertEquals(board.getSizeY(), result.length);
        assertEquals("X", result[0][0]);
        assertEquals("X", result[1][0]);
        assertEquals("X", result[2][0]);

        assertEquals("O", result[2][2]);
        assertEquals("O", result[1][1]);

        assertEquals("?", result[0][1]);
        assertEquals("?", result[0][2]);
        assertEquals("?", result[1][2]);
        assertEquals("?", result[2][1]);
    }

    @Test
    public void getAllPlayersFields() {
        Player a = new Player("A");
        Player b = new Player("B");

        board.doMove(new Move(a, new Point(0, 0)));
        board.doMove(new Move(a, new Point(1, 0)));
        board.doMove(new Move(a, new Point(2, 0)));

        board.doMove(new Move(b, new Point(2, 2)));
        board.doMove(new Move(b, new Point(1, 1)));

        List<BoardField> result = board.getSelector().getPlayerFields(b);
        assertEquals(2, result.size());
        assertEquals(new Point(2,2), new Point(result.get(1).getX(), result.get(1).getY()));
        assertEquals(new Point(1,1), new Point(result.get(0).getX(), result.get(0).getY()));
    }

    /**
     * Test of getRows method, of class Board.
     */
    @Test
    public void testGetColumns() {
        List<BoardField[]> columns = board.getSelector().getColumns();
        BoardField[] column1 = columns.get(2);

        assertEquals(3, columns.size());
        assertEquals(2, column1[0].getX());
        assertEquals(0, column1[0].getY());
    }

    /**
     * Test of getColumns method, of class Board.
     */
    @Test
    public void testGetRows() {
        List<BoardField[]> rows = board.getSelector().getRows();
        BoardField[] column1 = rows.get(1);

        assertEquals(3, rows.size());
        assertEquals(0, column1[0].getX());
        assertEquals(1, column1[0].getY());

    }

    /**
     * Test of getLeftRightDiagonal method, of class Board.
     */
    @Test
    public void getDiagonals() {
        List<BoardField[]> diagonals = board.getSelector().getDiagonals();
        assertEquals(2, diagonals.size());

        BoardField[] leftRightDiagonal = diagonals.get(0); // always starts with diagonal from 0,0 to max, max
        BoardField f1 = leftRightDiagonal[0];
        BoardField f2 = leftRightDiagonal[1];
        BoardField f3 = leftRightDiagonal[2];

        assertEquals(0, f1.getX());
        assertEquals(0, f1.getY());

        assertEquals(1, f2.getX());
        assertEquals(1, f2.getY());

        assertEquals(2, f3.getX());
        assertEquals(2, f3.getY());

    }

    /**
     * Test of getDiagonals method, of class Board.
     */
    @Test
    public void testGetDiagonals() {
    }

    @Test
    public void testGetAll() {
        List fields = board.getSelector().getAllFields();
        assertEquals(9, fields.size());
    }
}
