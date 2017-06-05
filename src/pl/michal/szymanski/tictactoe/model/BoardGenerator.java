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
package pl.michal.szymanski.tictactoe.model;



/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public abstract class BoardGenerator {

    public static BoardField[][] createBoard(int sizeX, int sizeY) {
        BoardField[][] board = new BoardField[sizeX][sizeY];

        for (int i = 0; i < sizeY; i++) {
            board[i] = fillRow(i, sizeY);
        }
        return board;
    }

    private static BoardField[] fillRow(int rowId, int max) {
        BoardField[] row = new BoardField[max];

        for (int i = 0; i < row.length; i++) {
            row[i] = new BoardField(i, rowId);
        }
        return row;
    }
}