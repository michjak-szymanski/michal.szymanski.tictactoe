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
package tictactoe.model;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class Board {

    private int sizeX;
    private int sizeY;
    private BoardField[][] matrix;

    public Board(Board board) {
        this.sizeX = board.sizeX;
        this.sizeY = board.sizeY;
        this.matrix = board.matrix;
    }

    public Board(int size) {
        this.sizeX = size;
        this.sizeY = size;
        this.matrix = BoardGenerator.createBoard(sizeX, sizeY);
    }

    public BoardSelector getSelector() {
        return new BoardSelector(this);
    }

    public BoardField[][] getBoard() {
        return matrix;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void clear() {
        this.matrix = BoardGenerator.createBoard(sizeX, sizeY);
    }

    public void doMove(Move move) {
        Player invoker = move.getInvoker().get();
        BoardField bf = matrix[move.getPoint().getY()][move.getPoint().getX()];
        bf.setOwner(invoker);
    }
}
