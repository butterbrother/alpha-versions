package Threads;

import java.lang.Math;

interface Params {
	int WORLD_WIDTH=60;
	int WORLD_HEIGHT=20;

	int CONSOLE_HEIGHT=25;

	//Statuses of cells
	int ALIVE=1;
	int DEAD=0;

	//Search status
	int FOUND=1;
	int NOT_FOUND=0;

	//Location
	int UP=1;
	int UP_RIGHT=2;
	int RIGHT=3;
	int DOWN_RIGHT=4;
	int DOWN=5;
	int DOWN_LEFT=6;
	int LEFT=7;
	int UP_LEFT=8;

}

final class World implements Params {
	int Worldstate = ALIVE;
	
	// Cells pointers
	private Cell Cells[] = new Cell[WORLD_WIDTH * WORLD_HEIGHT];;
	private boolean waiting = false;

	// Cells management functions
	// Add
	public void addCell(Cell cell) {
		try {
			while (! waiting) {
				wait();
			} 
		} catch (InterruptedException exc) {
			Worldstate = DEAD;
		}
		waiting = true;

		int state = NOT_FOUND;
		FND_CYCLE: {
			for (int i=0; i<(WORLD_WIDTH * WORLD_HEIGHT); i++) {
				if (Cells[i] == null) {
					state = FOUND;
					Cells[i]=cell;
					break FND_CYCLE;
				}
			}
		}
		if (state == NOT_FOUND)
			cell.status = DEAD;

		waiting = false;
		notifyAll();
	}
	// Delete
	public void dellCell(Cell cell) {
		try {
			while (! waiting) {
				wait();
			} 
		} catch (InterruptedException exc) {
			Worldstate = DEAD;
		}
		waiting = true;

		FND_CYCLE: {
			for (int i=0; i<(WORLD_WIDTH * WORLD_HEIGHT); i++) {
				if (Cells[i] != null)
					if (Cells[i] == cell) {
						Cells[i] = null;
						cell.status = DEAD;
						break FND_CYCLE;
					}
			}
		}

		waiting = false;
		notifyAll();
	}
	//Detect
	public int detectCells(Cell cell) {
		try {
			while (! waiting) {
				wait();
			} 
		} catch (InterruptedException exc) {
			Worldstate = DEAD;
		}
		waiting = true;

		for (int i=0; i<(WORLD_WIDTH * WORLD_HEIGHT); i++) {
			if (Cells[i] != null)
				if ((Math.abs(Cells[i].pos_X - cell.pos_X)) == 1)
					if ((Math.abs(Cells[i].pos_Y - cell.pos_Y)) == 1) {
						int hor = Cells[i].pos_X - cell.pos_X;
						int ver = Cells[i].pos_Y - cell.pos_Y;

						if ((hor == 0) && (ver == -1))
							return UP;
						if ((hor == 1) && (ver == -1))
							return UP_RIGHT;
						if ((hor == 1) && (ver == 0))
							return RIGHT;
						if ((hor == 1) && (ver == 1))
							return DOWN_RIGHT;
						if ((hor == 0) && (ver == 1))
							return DOWN;
						if ((hor == -1) && (ver == 1))
							return DOWN_LEFT;
						if ((hor == -1) && (ver == 0))
							return LEFT;
						if ((hor == -1) && (ver == -1))
							return UP_LEFT;
					}
		}

		waiting = false;
		notifyAll();

		return NOT_FOUND;
	}

	public void show() {
		//Clean screen
		for (int i=0; i<(CONSOLE_HEIGHT-(WORLD_HEIGHT+2)); i++)
			System.out.println("");

		//Top header
		for (int i=0; i<(WORLD_WIDTH+2); i++)
			System.out.print("_");
		System.out.println("");

		//Show table
		for (int y=0; y<WORLD_HEIGHT; y++) {
			System.out.print("|"); //Left border

			for (int x=0; x<WORLD_WIDTH; x++) {
				System.out.print(" ");
			}

			System.out.println("|"); //Right border
		}

		//Down
		for (int i=0; i<(WORLD_WIDTH+2); i++)
			System.out.print("_");
		System.out.println("");
	}
}

class Cell implements Params, Runnable {
	//world position
	int pos_X=0, pos_Y=0;

	//cell live status
	int status=ALIVE;
	int health=10;
	double energy=5;

	//Symlinks to world and this thread;
	World wld;
	Thread pnt;

	//Creating new cell
	Cell(World world, int initial_X, int initial_Y) {
		//Get started position
		this.pos_X = initial_X;
		this.pos_Y = initial_Y;

		//Symlink to world;
		wld = world;

		// New thread for new cell
		pnt = new Thread(this);
		pnt.start();
	}

	public void run() {
		try {
			while (status == ALIVE) {
				System.out.println("Nope"); //Nope
				energy -= 0.1;
				pnt.sleep(200);
				if ((health == 0) || (energy <= 0))
					status = DEAD;
			}
		} catch (InterruptedException exc) {
			System.out.println("");
		}
	}
}

class Net2 implements Params {
	public static void main(String args[]) {
		World wld = new World();
		try {
			while(true) {
				wld.show();
				Thread.sleep(500);
			}
		} catch (InterruptedException exc) {
			System.out.println("Щрд. МенR преrwali, товаrisch!");
			exc.printStackTrace();
			System.out.println("Останавливаю мир...");
			wld.Worldstate=DEAD;
		}
	}
}
