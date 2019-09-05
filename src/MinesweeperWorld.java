import javalib.impworld.*;
import java.awt.Color;

import javalib.worldimages.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import tester.*;

// to represent a game of Minesweeper with the given width, length and number of bombs
class MinesweeperWorld extends World {
  ArrayList<Cell> cells;
  int gridWidth; // horizontal number of cells
  int gridLength; // vertical number of cells
  int bombNum; // number of bombs across the board
  boolean ended;
  TextImage winmsg; //this only displays a message when the player clicks a mine
  int numClicks; //this is drawn to keep score for the player (FOR EXTRA CREDIT)
  int numRevealed; //when this reaches the number of non-bombed mines, the player wins

  MinesweeperWorld(int gridWidth, int gridLength, int bombNum) {
    this.gridWidth = gridWidth;
    this.gridLength = gridLength;
    this.bombNum = bombNum;
    this.cells = initCellList(); // populating the list with correct # of cells for this game
    this.winmsg = new TextImage("", 25, Color.RED);
    this.ended = false;
    this.numClicks = 0;
    this.numRevealed = 0;

    // connects each cell to its neighbors
    updateNeighbors(this.cells, gridWidth, gridLength);
    // places bombNum number of bombs randomly throughout the cells
    placeInBombs(this.cells, bombNum);
    // updates each cell's count of neighboring bombs
    updateCellBombCounts(this.cells);
  }
  
  // convenience constructor for tests, allows updateNeighbors, placeInBombs, updateCellBombCounts
  // to be called in examples class
  MinesweeperWorld() {
    this.gridWidth = 0;
    this.gridLength = 0;
    this.bombNum = 0;
    this.cells = null;
    this.winmsg = new TextImage("", 25, Color.RED);
    this.ended = false;
    this.numClicks = 0;
    this.numRevealed = 0;
  }

  // populates an ArrayList of Cells with the correct number of cells for this board.
  // also updates the ArrayList of Cells so as to connect each cell to it's neighbors in the 
  // displayed grid, as well as placing in bombs and updating the numbers indicating how many
  // bombs surround a given Cell
  ArrayList<Cell> initCellList() {
    ArrayList<Cell> newCells = new ArrayList<Cell>();

    while (newCells.size() < (this.gridLength * this.gridWidth)) {
      newCells.add(new Cell());
    }

    return newCells;
  }

  // takes an ArrayList of Cells and adds all neighboring cells to this Cell's neighbors ArrayList
  void updateNeighbors(ArrayList<Cell> newCells, int width, int length) {
    for (int i = 0; i < newCells.size(); i++) {
      Cell curCell = newCells.get(i);
      // top left corner
      if (i == 0) {
        curCell.neighbors.add(newCells.get(i + 1));
        curCell.neighbors.add(newCells.get(i + width));
        curCell.neighbors.add(newCells.get(i + width + 1));
      }
      // top row
      else if (i > 0 && i < width - 1) {
        curCell.neighbors.add(newCells.get(i - 1));
        curCell.neighbors.add(newCells.get(i + 1));
        curCell.neighbors.add(newCells.get(i + width - 1));
        curCell.neighbors.add(newCells.get(i + width));
        curCell.neighbors.add(newCells.get(i + width + 1));
      }
      // top right corner
      else if (i == width - 1) {
        curCell.neighbors.add(newCells.get(i - 1));
        curCell.neighbors.add(newCells.get(i + width - 1));
        curCell.neighbors.add(newCells.get(i + width));
      }
      // left side
      else if (i % width == 0 && i != ((width - 1) * width)
          && (i + width) != (width * length)) {
        curCell.neighbors.add(newCells.get(i - width));
        curCell.neighbors.add(newCells.get(i - width + 1));
        curCell.neighbors.add(newCells.get(i + 1));
        curCell.neighbors.add(newCells.get(i + width));
        curCell.neighbors.add(newCells.get(i + width + 1));
      }
      // right side
      else if ((i - (width - 1)) % width == 0 && i != (width * length) - 1) {
        curCell.neighbors.add(newCells.get(i - width - 1));
        curCell.neighbors.add(newCells.get(i - width));
        curCell.neighbors.add(newCells.get(i - 1));
        curCell.neighbors.add(newCells.get(i + width - 1));
        curCell.neighbors.add(newCells.get(i + width));
      }
      // bottom left corner
      else if ((i + width) == (width * length)) {
        curCell.neighbors.add(newCells.get(i - width));
        curCell.neighbors.add(newCells.get(i - width + 1));
        curCell.neighbors.add(newCells.get(i + 1));
      }
      // bottom side
      else if (i > ((width * length) - width)
          && i < ((width * length) - 1)) {
        curCell.neighbors.add(newCells.get(i - width - 1));
        curCell.neighbors.add(newCells.get(i - width));
        curCell.neighbors.add(newCells.get(i - width + 1));
        curCell.neighbors.add(newCells.get(i - 1));
        curCell.neighbors.add(newCells.get(i + 1));
      }
      // bottom right corner
      else if (i == this.cells.size() - 1) {
        curCell.neighbors.add(newCells.get(i - width - 1));
        curCell.neighbors.add(newCells.get(i - width));
        curCell.neighbors.add(newCells.get(i - 1));
      }
      // all middle area
      else {
        curCell.neighbors.add(newCells.get(i - width - 1));
        curCell.neighbors.add(newCells.get(i - width));
        curCell.neighbors.add(newCells.get(i - width + 1));
        curCell.neighbors.add(newCells.get(i - 1));
        curCell.neighbors.add(newCells.get(i + 1));
        curCell.neighbors.add(newCells.get(i + width - 1));
        curCell.neighbors.add(newCells.get(i + width));
        curCell.neighbors.add(newCells.get(i + width + 1));
      }
    }
  }

  // places the given number of bombs in random Cells throughout the ArrayList of Cells
  void placeInBombs(ArrayList<Cell> newCells, int bombNum) {
    int bombsPlaced = 0;
    Random rand = new Random();
    ArrayList<Integer> indexes = new ArrayList<Integer>();
    for (int i = 0; i < newCells.size(); i++) {
      indexes.add(i);
    }

    while (bombsPlaced != bombNum) {
      int randInt = rand.nextInt(indexes.size());
      newCells.get(indexes.get(randInt)).bomb = true;
      indexes.remove(randInt);
      bombsPlaced += 1;
    }
  }

  // iterates through a given ArrayList of Cells, counts the number of bombs present in a Cell's
  // neighbors ArrayList and sets the neighborhoodBombCount of the Cell to the count
  void updateCellBombCounts(ArrayList<Cell> newCells) {
    for (int i = 0; i < newCells.size(); i += 1) {
      newCells.get(i).neighborhoodBombCount = newCells.get(i).countBombs();
    }
  }
  
  //counts the number of bombs placed in this.cells
  //used to help in testing placeInBombs
  int countPlacedBombs() {
    int count = 0;
    for (Cell c : this.cells) {
      if (c.bomb) {
        count += 1;
      }
    }
    return count;
  }

  // makes a new WorldScene for the game of Minesweeper to be drawn on
  public WorldScene makeScene() {
    WorldScene s = this.getEmptyScene();
    draw(s);
    return s;
  }

  // draws all game Cells into a grid on top of the given WorldScene
  public void draw(WorldScene s) {
    int xCoord = 75;
    int yCoord = 0;
    for (int i = 0; i < cells.size(); i += 1) {
      if (i % this.gridWidth == 0) {
        xCoord = 75;
        yCoord = yCoord + 75;
      }
      else {
        xCoord = xCoord + 75;
      }
      WorldImage cell = new FrameImage(this.cells.get(i).drawnCell);
      s.placeImageXY(cell, xCoord, yCoord);
    }
    s.placeImageXY(this.winmsg, 20, 20);
    
    //displays the score of the user (FOR EXTRA CREDIT)
    s.placeImageXY(new TextImage("Score: " + Integer.toString(this.numClicks), 25, Color.BLACK),
        this.gridWidth * 75 - 20, this.gridLength * 75 + 75); 
  }
  
  //EFFECT: changes the board based on where the user clicked and what button they used.
  //        Draws a flag at the clicked cell if the user right-clicked.
  //        Reveals the clicked cell if the user left-clicked.
  public void onMouseClicked(Posn pos, String buttonName) {
    
    //prevents the user from clicking outside of the cell grid
    if (pos.x > this.gridWidth * 75 || pos.y > this.gridLength * 75
        || pos.x < 75 || pos.y < 75) {
      return;
    }
    
    //if the game is over, the user cannot affect the game board anymore 
    //and is prompted to close the window
    if (this.ended) {
      this.winmsg.text = "CLICK TO RESTART";
    }
    
    else {
      int rowNum = 0;
      int colNum = 0;
      this.numClicks += 1;
      
      for (double leftBound = 37.5; leftBound < (this.gridWidth * 75) + 37.5; 
          leftBound = leftBound + 75) {
        if (pos.x - 75 > leftBound) {
          rowNum++;
        }
      }
      
      for (double topBound = 37.5; topBound < (this.gridLength * 75) + 37.5; 
          topBound = topBound + 75) {
        if (pos.y - 75 > topBound) {
          colNum++;
        }
      }
      
      int index = rowNum + (colNum * this.gridWidth);
      Cell clickedCell = this.cells.get(index);
      int cellBombNum = clickedCell.neighborhoodBombCount;
      
      WorldImage newCell = new RectangleImage(75, 75, OutlineMode.SOLID, Color.darkGray);
      WorldImage oldCell = new RectangleImage(75, 75, OutlineMode.SOLID, Color.gray.brighter());
      WorldImage bomb = new CircleImage(25, OutlineMode.SOLID, Color.red.darker());
      WorldImage num = new TextImage(Integer.toString(cellBombNum), 50, Color.green.darker());
      WorldImage flag = new EquilateralTriangleImage(25, OutlineMode.SOLID, Color.ORANGE.darker());
      
      if (buttonName.equals("LeftButton")) {
        if (!clickedCell.flag) {
          if (clickedCell.bomb) { //when the user clicks a bomb they lose
            this.winmsg.text = "YOU LOSE!";
            clickedCell.drawnCell = new FrameImage(new OverlayImage(bomb, newCell));
            this.ended = true;
          }
          else if (cellBombNum == 0) {
            // flood fill
            clickedCell.drawnCell = new FrameImage(newCell);
            clickedCell.drawnYet = true;
            this.floodFill(index);
          }
          else if (cellBombNum > 0) {
            clickedCell.drawnCell = new FrameImage(new OverlayImage(num, newCell));
            clickedCell.drawnYet = true;
          }
        }
      }
      
      //placing a flag doesn't effect the "drawn" status of a cell,
      //so it will still be affected by floodfill.
      //It's not explicitly stated in the instructions whether this is correct or incorrect
      else if (buttonName.equals("RightButton")) {
        if (clickedCell.drawnCell.equals(oldCell) || 
            clickedCell.drawnCell.equals(new FrameImage(new OverlayImage(flag, oldCell)))) {
        
          if (clickedCell.flag) {
            clickedCell.drawnCell = oldCell;
          }
          else {
            clickedCell.drawnCell = new FrameImage(new OverlayImage(flag, oldCell));
          }
          clickedCell.flag = !clickedCell.flag;
        }
      }
      
      //updates the number of Cells that have been revealed after the user clicked
      for (Cell c : this.cells) {
        if (c.drawnYet && !c.markedYet) {
          this.numRevealed += 1;
          c.markedYet = true;
        }
      }
      
      //when the user has revealed all cells that don't have mines on them, they win
      if (this.numRevealed == this.cells.size() - this.bombNum) {
        this.winmsg.text = "YOU WIN!";
        this.ended = true;
      }
    }
  }
  
  //EFFECT: iterates over each cell's neighbors and draws them in the appropriate way
  //        based on their adjacency to bombs or whether or not they have already been drawn
  void floodFill(int index) {
    Iterator<Cell> i = this.cells.get(index).iterator();
    WorldImage newCell = new RectangleImage(75, 75, OutlineMode.SOLID, Color.darkGray);
    
    while (i.hasNext()) {
      Cell cell = i.next();
      WorldImage num = 
          new TextImage(Integer.toString(cell.neighborhoodBombCount), 50, Color.green.darker());
      
      if (cell.neighborhoodBombCount == 0 && !cell.drawnYet) {
        cell.drawnCell = new FrameImage(newCell);
        cell.drawnYet = true;
        floodFill(this.cells.indexOf(cell));
      }
      
      else if (cell.neighborhoodBombCount > 0) {
        cell.drawnCell = new FrameImage(new OverlayImage(num, newCell));
        cell.drawnYet = true;
      }
    }
  }
}


//to represent a single square cell of a game of Minesweeper
class Cell implements Iterable<Cell> {
  
  ArrayList<Cell> neighbors;
  int neighborhoodBombCount;
  boolean bomb;
  boolean flag;
  boolean drawnYet; //unaffected by flag placement
  boolean markedYet; //checks if this cell has been marked (only marked if it has benn revealed)
  WorldImage drawnCell; // the current drawn state of this Cell

  Cell() {
    this.neighbors = new ArrayList<Cell>();
    this.neighborhoodBombCount = 0;
    this.bomb = false;
    this.flag = false;
    this.drawnYet = false;
    this.markedYet = false;
    this.drawnCell = new RectangleImage(75, 75, OutlineMode.SOLID, Color.gray.brighter());
  }

  //returns a CellIterator to iterate over this Cell's neighbors list
  public Iterator<Cell> iterator() {
    return new CellIterator(this);
  }

  //counts the number of neighbors this Cell has which contain a bomb
  int countBombs() {
    int count = 0;

    for (int i = 0; i < neighbors.size(); i++) {
      if (neighbors.get(i).bomb) {
        count++;
      }
    }
    return count;
  }
}

class CellIterator implements Iterator<Cell> {
  Cell cell;
  int index;
  
  CellIterator(Cell cell) {
    this.cell = cell;
    this.index = 0;
  }
  
  //checks if there are more neighbors of the Cell to check
  public boolean hasNext() {
    return index < cell.neighbors.size();
  }
  
  //returns the current Cell
  //EFFECT: increases the index used in the neighbor list by 1
  public Cell next() {
    Cell tmp = cell.neighbors.get(index);
    this.index++;
    return tmp;
  }
}


class ExamplesMinesweeper {
  
  MinesweeperWorld board1;
  MinesweeperWorld board2;
  MinesweeperWorld board3;
  MinesweeperWorld board4;

  ArrayList<Cell> cells1;
  ArrayList<Cell> cells2;
  ArrayList<Cell> cells3;
  ArrayList<Cell> cells4;

  void initConditions() {
    
    this.board1 = new MinesweeperWorld(2, 2, 4);
    //we place 4 mines in board1 to make it not random when we test onMouseClicked
    this.board2 = new MinesweeperWorld(4, 4, 6);
    this.board3 = new MinesweeperWorld(9, 9, 10);
    this.board4 = new MinesweeperWorld(12, 12, 30);

    this.cells1 = new ArrayList<Cell>(); // 2 x 2 --> length = 4
    this.cells2 = new ArrayList<Cell>(); // 4 x 4 --> length = 16
    this.cells3 = new ArrayList<Cell>(); // 9 x 9 --> length = 81
    this.cells4 = new ArrayList<Cell>(); // 12 x 12 > length = 144
    
    while (this.cells1.size() < 4) {
      this.cells1.add(new Cell());
    }

    while (this.cells2.size() < 16) {
      this.cells2.add(new Cell());
    }

    while (this.cells3.size() < 81) {
      this.cells3.add(new Cell());
    }

    while (this.cells4.size() < 144) {
      this.cells4.add(new Cell());
    }

    this.board1.updateNeighbors(this.cells1, 2, 2);
    this.board2.updateNeighbors(this.cells2, 4, 4);
    this.board3.updateNeighbors(this.cells3, 9, 9);
    this.board4.updateNeighbors(this.cells4, 12, 12);

    this.board1.placeInBombs(this.cells1, 4);
    this.board2.placeInBombs(this.cells2, 6);
    this.board3.placeInBombs(this.cells3, 10);
    this.board4.placeInBombs(this.cells4, 30);

    this.board1.updateCellBombCounts(this.cells1);
    this.board2.updateCellBombCounts(this.cells2);
    this.board3.updateCellBombCounts(this.cells3);
    this.board4.updateCellBombCounts(this.cells4);
  }
  
  void testInitCellList(Tester t) {
    this.initConditions();
    
    t.checkExpect(this.cells1.size(), 4);
    t.checkExpect(this.cells2.size(), 16);
    t.checkExpect(this.cells3.size(), 81);
    t.checkExpect(this.cells4.size(), 144);
  }
  
  void testUpdateNeighbors(Tester t) {
    this.initConditions();
    
    t.checkExpect(this.cells1.get(0).neighbors.size(), 3);
    t.checkExpect(this.cells2.get(0).neighbors.size(), 3);
    t.checkExpect(this.cells2.get(2).neighbors.size(), 5);
    t.checkExpect(this.cells2.get(6).neighbors.size(), 8);
    t.checkExpect(this.cells3.get(0).neighbors.size(), 3);
    t.checkExpect(this.cells3.get(1).neighbors.size(), 5);
    t.checkExpect(this.cells3.get(12).neighbors.size(), 8);
  }
  
  void testPlaceInBombs(Tester t) {
    this.initConditions();
    
    t.checkExpect(this.board1.countPlacedBombs(), 4);
    t.checkExpect(this.board2.countPlacedBombs(), 6);
    t.checkExpect(this.board3.countPlacedBombs(), 10);
    t.checkExpect(this.board4.countPlacedBombs(), 30);
  }
  
  void testUpdateCellBombCounts(Tester t) {
    this.initConditions();
    
    t.checkExpect(this.board1.cells.get(0).neighborhoodBombCount == 3, true);
    t.checkExpect(this.board2.cells.get(0).neighborhoodBombCount >= 0, true);
    t.checkExpect(this.board3.cells.get(0).neighborhoodBombCount >= 0, true);
    t.checkExpect(this.board4.cells.get(0).neighborhoodBombCount >= 0, true);
  }
  
  void testFloodFill(Tester t) {
    this.initConditions();
    
    this.board1.floodFill(0);
    t.checkExpect(this.board1.numRevealed >= 0, true);
  }
  
  void testOnMouseClicked(Tester t) {
    this.initConditions();
    
    t.checkExpect(this.board2.numRevealed, 0);
    this.board2.onMouseClicked(new Posn(80, 80), "RightButton");
    t.checkExpect(this.board2.numRevealed, 0);
    
    this.board1.onMouseClicked(new Posn(120, 120), "LeftButton");
    t.checkExpect(this.board1.ended, true);
  }
  
  void testCountPlacedBombs(Tester t) {
    this.initConditions();
    
    t.checkExpect(this.board1.countPlacedBombs(), 4);
    t.checkExpect(this.board2.countPlacedBombs(), 6);
    t.checkExpect(this.board3.countPlacedBombs(), 10);
    t.checkExpect(this.board4.countPlacedBombs(), 30);
  }
  
  void testCountBombs(Tester t) {
    this.initConditions();
    
    t.checkExpect(this.cells1.get(0).countBombs() >= 0, true);
  }
  
  void testMakeScene(Tester t) {
    //board1.bigBang(250, 250);
    //board2.bigBang(450, 450);
    //board3.bigBang(750, 800);
    board4.bigBang(1000, 1000);
  }
}

