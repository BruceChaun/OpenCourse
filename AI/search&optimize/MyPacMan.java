package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.net.PortUnreachableException;
import java.util.*;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	private final int BFS_MIN_DIST = 50; // minimum distance in BFS
	private final int DFS_MIN_DIST = 20; // minimum distance in DFS
	private final int UNLIMITED = -1;    // there is no depth limitation
	private final int LIMIT = 20;        // depth limit in DLS
	private final int MAX_STATE = 10000;  // cutoff when BFS reaches such many states
	private MOVE CUTOFF = MOVE.NEUTRAL;  // cutoff in DLS
	private final int ID_MAX_DEPTH = 30; // maximum depth in IDS
	private final int SEQ_MAX_LEN = 10;   // number of sequence of actions in optimization
    final int seqLen = 5;               // how many moves in genome
	
	public MOVE getMove(Game game, long timeDue) 
	{
		//Place your game logic here to play the game as Ms Pac-Man

//		return getRandMove(game, timeDue, false);
//        return getBFSMove(game, timeDue);
//        return getDLSMove(game, timeDue);
//        return getDFSMove(game, timeDue);
//        return getIDSearch(game, timeDue);
//        return getAStarSearch(game, timeDue);
		return getHillMove(game, timeDue);
//		return getSAMove(game, timeDue);
//        return getGAMove(game, timeDue);
//        return getEAMove(game, timeDue);
	}

	/*
     *  randomly pick one move from possible moves
     */
	private MOVE getRandMove(Game game, long timeDue, boolean neutral) {
		int current = game.getPacmanCurrentNodeIndex();

		MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
		if (neutral)
			moves = game.getPossibleMoves(current);

		if (moves.length > 0) {
			return moves[new Random().nextInt(moves.length)];
		}

		return game.getPacmanLastMoveMade().opposite();
	}

	/*
     *  given current game state and actions of pacman and ghosts,
     *  return the successive game state.
     */
	private Game nextGameState(Game game, MOVE pacmanMove, EnumMap<Constants.GHOST, MOVE> ghostsMoves) {
		Game gameCopy = game.copy();
		gameCopy.advanceGameWithoutReverse(pacmanMove, ghostsMoves);
//        gameCopy.updatePacMan(pacmanMove);
//        gameCopy.updateGhosts(ghostsMoves);
		return gameCopy;
	}

	/*
     *  backtracking from the future node to get the next move using hash map
     *  called by BFS method
     */
	private MOVE backtrack(Game game, HashMap<Game, Game> backpointer, int level, boolean opposite) {
		Game currentGame = game;
		while (--level > 0) {
			currentGame = backpointer.get(currentGame);
		}

		if (opposite) {
			Game last = backpointer.get(currentGame);
			MOVE[] moves = last.getPossibleMoves(last.getPacmanCurrentNodeIndex());
			int rand = new Random().nextInt(moves.length);
			return moves[rand];
		}
		return currentGame.getPacmanLastMoveMade();
	}

	/*
     *  back track from the future node to get the next move using stack
     *  called by DFS and its variants
     */
	private MOVE backtrack(Stack<Game> stack) {
		Game game = stack.peek();
		while (stack.size() > 2) {
			game = stack.pop();
		}
		return game.getPacmanLastMoveMade();
	}

	/*
     *  get possible moves of ghosts
     */
	private int getGhostsMoves(Game game, List<Constants.GHOST> ghosts, List<MOVE[]> ghostsPossibleMoves, int[] maxCnt) {
		for (Constants.GHOST ghost : Constants.GHOST.values()) {
			int ghostLoc = game.getGhostCurrentNodeIndex(ghost);
			if (ghostLoc != -1) {
				ghosts.add(ghost);
				ghostsPossibleMoves.add(
						game.getPossibleMoves(ghostLoc, game.getGhostLastMoveMade(ghost))
				);
			}
		}

		int nGhostLeft = ghosts.size();
		int zeros = 0;
		for (int i = 0; i < nGhostLeft; i++) {
			maxCnt[i] = ghostsPossibleMoves.get(i).length;
			if (maxCnt[i] == 0) zeros++;
		}

		return nGhostLeft - zeros;
	}

	/*
     *  use bfs to search state
     */
	private MOVE getBFSMove(Game game, long timeDue) {
		Queue<Game> gameQueue = new LinkedList<Game>();
		HashMap<Game, Game> backpointer = new HashMap<Game, Game>();

		// add root, i.e. current game state, expand here
		gameQueue.add(game.copy());
		gameQueue.add(null); // null is a canary of the end of a level
		int level = 0;

		while (!gameQueue.isEmpty()) {
//			if (gameQueue.size() > MAX_STATE) {
//				// too many states, change to random mode
//				gameQueue.clear();
//				return getAliveMove(game);
//			}

			Game gameNode = gameQueue.poll();
			if (gameNode == null) {
				level++;
				gameQueue.add(null);
				if (gameQueue.peek() == null) {
					gameQueue.clear();
					backpointer.clear();
					break;
				}
				continue;
			}

			int current = gameNode.getPacmanCurrentNodeIndex();
			MOVE[] moves = gameNode.getPossibleMoves(current, gameNode.getPacmanLastMoveMade());
//            MOVE[] moves = gameNode.getPossibleMoves(current);

			// get possible moves of current ghosts
//			List<Constants.GHOST> ghosts = new ArrayList<Constants.GHOST>();
//			List<MOVE[]> ghostsPossibleMoves = new ArrayList<MOVE[]>();
//			int[] ptr = new int[Constants.GHOST.values().length];
//			int[] maxCnt = new int[Constants.GHOST.values().length];
//			int nGhostLeft = getGhostsMoves(gameNode, ghosts, ghostsPossibleMoves, maxCnt);
			EnumMap<Constants.GHOST, MOVE> ghostsMoves = new EnumMap<Constants.GHOST, MOVE>(Constants.GHOST.class);
			for (Constants.GHOST ghost : Constants.GHOST.values()) {
				ghostsMoves.put(ghost, gameNode.getGhostLastMoveMade(ghost));
			}

			// for each possible moves of pacman
			for (MOVE m : moves) {
				// for each possible moves of ghosts
//				while (true) {
//					boolean stop = false;
//					if (nGhostLeft > 0) {
//						int zeros = 0;
//						for (int i = 0; i < nGhostLeft; i++) {
//							if (maxCnt[i] > 0)
//								ghostsMoves.put(ghosts.get(i), ghostsPossibleMoves.get(i)[ptr[i]]);
//							else zeros++;
//						}
//
//						if (nGhostLeft - zeros == 0)
//							stop = true;
//
//						ptr[0]++;
//						for (int i = 0; i < nGhostLeft; i++) {
//							if (maxCnt[i] > 0 && ptr[i] == maxCnt[i]) {
//								ptr[i] = 0;
//								if (i < nGhostLeft - 1)
//									ptr[i + 1]++;
//								else
//									stop = true;
//							}
//						}
//					} else {
//						stop = true;
//					}

					Game nextGame = nextGameState(gameNode, m, ghostsMoves);

					if (!nextGame.wasPacManEaten()) {
						boolean eatGhosts = false;
//						boolean runAway = false;
//						if (nGhostLeft > 0) {
							int myLoc = nextGame.getPacmanCurrentNodeIndex();
							for (Constants.GHOST ghost : Constants.GHOST.values()) {
								int loc = nextGame.getGhostCurrentNodeIndex(ghost);
								int dist = nextGame.getManhattanDistance(myLoc, loc);
								if (nextGame.isGhostEdible(ghost) && dist < BFS_MIN_DIST) {
									eatGhosts = true;
									break;
								}

//								if (dist < BFS_MIN_DIST / 2 && !nextGame.isGhostEdible(ghost))
//									runAway = true;
//
//								if (eatGhosts || runAway) break;
							}
//						}

						if (eatGhosts) {
							backpointer.put(nextGame, gameNode);
							return backtrack(nextGame, backpointer, level + 1, false);
//                        } else if (runAway) {
////                            continue;
//                            backpointer.put(nextGame, gameNode);
//                            return backtrack(nextGame, backpointer, level + 1, true);
						} else if (nextGame.wasPillEaten() || nextGame.wasPowerPillEaten()) {
							backpointer.put(nextGame, gameNode);
							return backtrack(nextGame, backpointer, level + 1, false);
						} else {
							backpointer.put(nextGame, gameNode);
							gameQueue.add(nextGame);
						}
					}

//					if (stop) {
//						break;
//					}
//				}
			}
		}

		return getAliveMove(game);
	}

	/*
     *  use depth-limit search to search the state
     */
	private MOVE getDLSMove(Game game, long timeDue) {
		HashSet<Integer> set = new HashSet<Integer>(); // pacman location
		set.add(game.getPacmanCurrentNodeIndex());

		MOVE nextMove = DLS(game, LIMIT, set);
		if (nextMove == null) {
//			return game.getPacmanLastMoveMade().opposite();
			return getAliveMove(game);
		}

		if (nextMove == CUTOFF) {
			return getAliveMove(game);
		}

		return nextMove;
	}

	/*
     use dfs to search the state
     */
	private MOVE getDFSMove(Game game, long timeDue) {
		HashSet<Integer> set = new HashSet<Integer>(); // pacman location
		set.add(game.getPacmanCurrentNodeIndex());

		MOVE nextMove = DLS(game, UNLIMITED, set);
		set.clear();
		if (nextMove == null) {
			return game.getPacmanLastMoveMade().opposite();
		}

		return nextMove;
	}

	/*
     *  depth-limit recursive function
     */
	private MOVE DLS(Game game, int limit, HashSet<Integer> set) {
		// cutoff failure
		if (limit == 0)
			return CUTOFF;

		// get possible moves of pacman
		int current = game.getPacmanCurrentNodeIndex();
		MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
//        MOVE[] moves = game.getPossibleMoves(current);

		// get possible moves of current ghosts
		List<Constants.GHOST> ghosts = new ArrayList<Constants.GHOST>();
		List<MOVE[]> ghostsPossibleMoves = new ArrayList<MOVE[]>();

		int[] maxCnt = new int[Constants.GHOST.values().length];
		int nGhostLeft = getGhostsMoves(game, ghosts, ghostsPossibleMoves, maxCnt);

		int cutoff = 0;
		for (MOVE m : moves) {
			int[] ptr = new int[Constants.GHOST.values().length];
			while (true) {
				EnumMap<Constants.GHOST, MOVE> ghostsMoves = generateNextStates(ghosts, ghostsPossibleMoves, ptr, maxCnt);

				Game nextGame = nextGameState(game, m, ghostsMoves);
				// check game state -- whether to search deeper
				if (nextGame.wasPacManEaten()) {
					break;
				} else if ( set.contains( nextGame.getPacmanCurrentNodeIndex() ) ) {
					break;
				}

				boolean eatGhosts = false;
				if (nGhostLeft > 0) {
					int myLoc = nextGame.getPacmanCurrentNodeIndex();
					for (Constants.GHOST ghost : Constants.GHOST.values()) {
						int loc = nextGame.getGhostCurrentNodeIndex(ghost);
						int dist = nextGame.getManhattanDistance(myLoc, loc);
						if (nextGame.isGhostEdible(ghost) && dist < DFS_MIN_DIST) {
							eatGhosts = true;
							break;
						}
					}
				}

				set.add(nextGame.getPacmanCurrentNodeIndex());
				if (eatGhosts || nextGame.wasPillEaten() || nextGame.wasPowerPillEaten()) {
					if (limit == UNLIMITED) return m;
					if (limit < LIMIT) return m;
					return nextGame.getPacmanLastMoveMade();
				}

				if (limit != UNLIMITED) {
					MOVE move = DLS(nextGame, limit - 1, set);
					if (move == CUTOFF) {
						cutoff++;
					} else if (move != null) {
						if (limit < LIMIT) return m;
						return move;
					}
				} else {
					MOVE move = DLS(nextGame, UNLIMITED, set);
					if (move != null) {
						return m;
					}
				}

				if (ghostsMoves == null) {
					break;
				}
			}
		}

		if (cutoff == moves.length)
			return CUTOFF;

		return null;
	}

	/*
     *  4 ghosts have many possible combinations of moves, each represents the move state of ghosts
     *  given all the available ghosts and their possible moves, and pointer array pointing to the
     *  current state, max count array stores total number of moves of a ghost
     */
	private EnumMap<Constants.GHOST, MOVE> generateNextStates(List<Constants.GHOST> ghosts, List<MOVE[]> ghostsPossibleMoves,
															  int[] ptr, int[] maxCnt) {
		EnumMap<Constants.GHOST, MOVE> ghostsNextMoves = new EnumMap<Constants.GHOST, MOVE>(Constants.GHOST.class);
		int nGhostLeft = ghosts.size();

		if (nGhostLeft > 0) {
			for (int i = 0; i < nGhostLeft; i++) {
				if (maxCnt[i] > 0)
					ghostsNextMoves.put(ghosts.get(i), ghostsPossibleMoves.get(i)[ptr[i]]);
			}

			ptr[0]++;
			for (int i = 0; i < nGhostLeft; i++) {
				if (maxCnt[i] > 0) {
					if (ptr[i] == maxCnt[i]) {
						ptr[i] = 0;
						if (i < nGhostLeft - 1)
							ptr[i + 1]++;
						else
							return null;
					} else break;
				}
			}
		}
		return ghostsNextMoves;
	}

	/*
     *  iterative deepening search (IDS)
     */
	private MOVE getIDSearch(Game game, long timeDue) {
		int i = 1;
		while (i++ < ID_MAX_DEPTH) {
			MOVE move = getDLSMove(game, timeDue);
			if (move != null && move != CUTOFF)
				return move;
		}
		return getRandMove(game, timeDue, false);
	}

	/*
     *  A* search algorithm, f(n) = g(n) + h(n)
     *  g(n): cost from current state to state n
     *  h(n): minimum cost from state n to goal state
     *  f(n): total cost from current state to goal state
     */
	private MOVE getAStarSearch(Game game, long timeDue) {
		Game gameCopy = game.copy();
		int dest = getDest(gameCopy);
		if (dest == -1) {
			return getAliveMove(game);
		}

		Stack<Game> stack = new Stack<Game>();
		stack.push(gameCopy);

		while (true) {
			if (stack.size() > MAX_STATE) {
				stack.clear();
				return getAliveMove(game);
//				return getRandMove(game, timeDue, false);
			}
			Game nextGame = getNextOPTGame(stack.peek(), dest);
			if (nextGame != null) {
				stack.push(nextGame);
				if (nextGame.getPacmanCurrentNodeIndex() == dest)
					return backtrack(stack);
			} else break;
		}

		return getAliveMove(game);
	}

	/*
     *  get nearest edible ghost or pill
     */
	private int getDest(Game game) {
		// get nearest edible ghost within attack distance
		int ghostDist = Integer.MAX_VALUE;
		int loc = -1;
		int current = game.getPacmanCurrentNodeIndex();
		for (Constants.GHOST ghost : Constants.GHOST.values()) {
			if (game.getGhostEdibleTime(ghost) > 0) {
				int ghostLocation = game.getGhostCurrentNodeIndex(ghost);
				if (ghostLocation != -1) {
					int dist = game.getShortestPathDistance(current, ghostLocation);
					if (dist < BFS_MIN_DIST && dist < ghostDist) {
						loc = ghostLocation;
						ghostDist = dist;
					}
				}
			}
		}
		if (loc != -1) {
			return loc;
		}

		// get nearest edible pill, copy from POPacMan
		int[] pills = game.getPillIndices();
		int[] powerPills = game.getPowerPillIndices();

		ArrayList<Integer> targets = new ArrayList<Integer>();

		for (int i = 0; i < pills.length; i++) {
			//check which pills are available
			Boolean pillStillAvailable = game.isPillStillAvailable(i);
			if (pillStillAvailable == null) continue;
			if (game.isPillStillAvailable(i)) {
				targets.add(pills[i]);
			}
		}

		for (int i = 0; i < powerPills.length; i++) {            //check with power pills are available
			Boolean pillStillAvailable = game.isPillStillAvailable(i);
			if (pillStillAvailable == null) continue;
			if (game.isPowerPillStillAvailable(i)) {
				targets.add(powerPills[i]);
			}
		}

		if (!targets.isEmpty()) {
			int[] targetsArray = new int[targets.size()];        //convert from ArrayList to array

			for (int i = 0; i < targetsArray.length; i++) {
				targetsArray[i] = targets.get(i);
			}

			//return the next direction once the closest target has been identified
			return game.getClosestNodeIndexFromNodeIndex(current, targetsArray, Constants.DM.PATH);
		}

		return -1;
	}

	/*
     *  get next move in A* search
     */
	private Game getNextOPTGame(Game game, int dest) {
		// get possible moves of pacman
		int current = game.getPacmanCurrentNodeIndex();
		Game optGame = null;
		int minCost = Integer.MAX_VALUE;
//        MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
		MOVE[] moves = game.getPossibleMoves(current);

		// get possible moves of current ghosts
		List<Constants.GHOST> ghosts = new ArrayList<Constants.GHOST>();
		List<MOVE[]> ghostsPossibleMoves = new ArrayList<MOVE[]>();

		int[] maxCnt = new int[Constants.GHOST.values().length];
		int nGhostLeft = getGhostsMoves(game, ghosts, ghostsPossibleMoves, maxCnt);

		for (MOVE m : moves) {
			Game nextGame = nextGameState(game, m, new EnumMap<Constants.GHOST, MOVE>(Constants.GHOST.class));
			int nextLoc = nextGame.getPacmanCurrentNodeIndex();
			int cost = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), nextLoc); // g(n)
			cost += nextGame.getManhattanDistance(nextLoc, dest); // h(n)
			if (nextGame.wasPacManEaten()) {
				cost = Integer.MAX_VALUE;
			}

			if (cost < minCost) {
				minCost = cost;
				optGame = nextGame;
			}
		}

		return optGame;
	}

	/*
     *  decouple loop
     */
	private boolean isLoop(Game game) {
		int loc = getDest(game);
		if (loc == -1) return false;

		Game next = getNextOPTGame(game, loc);
		if (next == null) return false;

		int nextLoc = getDest(next);
		Game next2 = getNextOPTGame(next, nextLoc);
		if (next2 == null) return false;

		if (game.getPacmanCurrentNodeIndex() == next2.getPacmanCurrentNodeIndex())
			return true;
		return false;
	}

	/*
     *  hill climbing optimization, local search
     */
	private MOVE getHillMove(Game game, long timeDue) {
		int current = game.getPacmanCurrentNodeIndex();
		MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
//        MOVE[] moves = game.getPossibleMoves(current);

		Queue<Game> queue = new LinkedList<Game>();
		queue.add(game.copy());
		HashMap<Game, Game> backpointer = new HashMap<Game, Game>();
		int remaining = SEQ_MAX_LEN;
		while (remaining > 0) {
			Game nextGame = recursiveHill(queue, backpointer);
			if (nextGame != null) {
				MOVE m = backtrack(nextGame, backpointer, SEQ_MAX_LEN - remaining + 1, false);
				return m;
			} else if (queue.size() == 0)
				break;

			remaining--;
		}

		return getAliveMove(game);
//        return getAStarSearch(game, timeDue);
	}

	/*
     *  recursively call to get the game score
     */
	private Game recursiveHill(Queue<Game> queue, HashMap<Game, Game> backpointer) {
		int n = queue.size();
		if (n == 0)
			return null;

		Game next = null;
		int bestScore = queue.peek().getScore();

		while (n > 0) {
			n--;
			Game game = queue.poll();
			int current = game.getPacmanCurrentNodeIndex();
			MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());
//            MOVE[] moves = game.getPossibleMoves(current);

			for (MOVE m : moves) {
				Game nextGame = game.copy();
				int score = -1;
				while (true) {
					EnumMap<Constants.GHOST, MOVE> ghostMoves = new EnumMap<Constants.GHOST, MOVE>(Constants.GHOST.class);
					for (Constants.GHOST ghost : Constants.GHOST.values()) {
						int loc = game.getGhostCurrentNodeIndex(ghost);
						MOVE[] ghostMove = game.getPossibleMoves(loc, game.getGhostLastMoveMade(ghost));
						if (ghostMove != null && ghostMove.length > 0) {
							MOVE nextMove = ghostMove[new Random().nextInt(ghostMove.length)];
							ghostMoves.put(ghost, nextMove);
						}
					}

					MOVE[] ms = nextGame.getPossibleMoves(nextGame.getPacmanCurrentNodeIndex(), nextGame.getPacmanLastMoveMade());
					if (ms == null) {
						System.out.println("ms is null");
					}
					nextGame = nextGameState(nextGame, m, ghostMoves);
					if (nextGame.wasPacManEaten()) {
						break;
					}

					if (!ms[0].equals(m) || ms.length > 1) {
						backpointer.put(nextGame, game);
						score = nextGame.getScore();
						if (score > bestScore) {
							bestScore = score;
							next = nextGame;
						} else {
							queue.add(nextGame);
						}
						break;
					}
				}
			}
		}

		return next;
	}

	private MOVE getAliveMove(Game game) {
		int current = game.getPacmanCurrentNodeIndex();
		MOVE[] moves = game.getPossibleMoves(current, game.getPacmanLastMoveMade());

		EnumMap<Constants.GHOST, MOVE> ghostMoves = new EnumMap<Constants.GHOST, MOVE>(Constants.GHOST.class);
		for (Constants.GHOST ghost : Constants.GHOST.values()) {
			ghostMoves.put(ghost, game.getGhostLastMoveMade(ghost));
		}

		for (int i = 0; i < 8; i++) {
			MOVE m = moves[new Random().nextInt(moves.length)];
			Game next = nextGameState(game, m, ghostMoves);
			if (!next.wasPacManEaten())
				return m;
		}

		return game.getPacmanLastMoveMade().opposite();
	}

	/*
	 *	simulated annealing
	 */
	private MOVE getSAMove(Game game, long timeDue) {
        HashMap<Game, Game> backpointer = new HashMap<Game, Game>();

		double T = 10;
		double diff = 1;
        Game current = game.copy();
        int level = 0;
		while (T > 0) {
            int currentScore = current.getScore();

			// generate neibors
			List<Game> nextStates = findCrossRoad(current);

			int n = nextStates.size();
			if (n == 0) break;

			int rand = new Random().nextInt(n);
			Game next = nextStates.get(rand);

			int score = next.getScore();
			if (score > currentScore) {
                backpointer.put(next, current);
                current = next;
                level++;
                break;
            } else if (Math.exp((score - currentScore - 1) / T) > new Random().nextDouble()) {
                backpointer.put(next, current);
                current = next;
                level++;
            }

            T -= diff;
		}

		if (backpointer.size() == 0) return getAliveMove(game);
        return backtrack(current, backpointer, level, false);
	}

	private List<Game> findCrossRoad(Game current) {
		List<Game> nextStates = new ArrayList<Game>();
		int currentLoc = current.getPacmanCurrentNodeIndex();
		MOVE[] moves = current.getPossibleMoves(currentLoc, current.getPacmanLastMoveMade());

		for (MOVE m : moves) {
			Game nextGame = current.copy();
			while (true) {
				EnumMap<Constants.GHOST, MOVE> ghostMoves = new EnumMap<Constants.GHOST, MOVE>(Constants.GHOST.class);
				for (Constants.GHOST ghost : Constants.GHOST.values()) {
					int loc = current.getGhostCurrentNodeIndex(ghost);
					MOVE[] ghostMove = current.getPossibleMoves(loc, current.getGhostLastMoveMade(ghost));
					if (ghostMove != null && ghostMove.length > 0) {
						MOVE nextMove = ghostMove[new Random().nextInt(ghostMove.length)];
						ghostMoves.put(ghost, nextMove);
					}
				}

				MOVE[] ms = nextGame.getPossibleMoves(nextGame.getPacmanCurrentNodeIndex(), nextGame.getPacmanLastMoveMade());
				nextGame = nextGameState(nextGame, m, ghostMoves);
				if (nextGame.wasPacManEaten()) {
					break;
				}

				if (!ms[0].equals(m) || ms.length > 1) {
					nextStates.add(nextGame);
					break;
				}
			}
		}

		return nextStates;
	}

	class Population {
        MOVE[] moves = new MOVE[seqLen];
        int fitness = 0;

        public void copy(Population that) {
            this.fitness = that.fitness;
            for (int i = 0; i < that.moves.length; i++) {
                this.moves[i] = that.moves[i];
            }
        }
    }

    /*
     *  Genetic algorithm (with crossover)
     */
    private MOVE getGAMove(Game game, long timeDue) {
        return getEvolutionMove(game, true);
    }

    /*
     *  Evolutionary algorithm (mutation only)
     */
    private MOVE getEAMove(Game game, long timeDue) {
        return getEvolutionMove(game, false);
    }

    /*
     *  evolutionary algorithm
     *
     *  parameters
     *  @crossOver indicates whether a crossover is involved.
     */
	private MOVE getEvolutionMove(Game game, boolean crossOver) {
		final int maxGen = 30;   // max iteration
		final int nPop = 30;      // how many population
        final double mutationRate = 0.05;
        final int mu = nPop / 3; // simple mu + lambda where mu == lambda

        // order by fitness
		Population[] population = init(game, nPop, seqLen);

		Population[] newPopulation = new Population[nPop];
		for (int i = 0; i < nPop; i++) {
			newPopulation[i] = new Population();
		}
        int gen = 0;

        while (gen++ < maxGen) {
            if (crossOver) {
                for (int i = 0; i < nPop; i++) {
                    // select parents
                    Population father = select(population);
					if (father == null) return getAliveMove(game);
                    Population mother = select(population);

                    // produce child -- crossover
                    Population child = reproduce(father, mother);

                    // mutation is possible
                    if (new Random().nextDouble() <= mutationRate)
                        mutate(child);

                    // re-evaluate fitness of next generation
                    child.fitness = evaluateFitness(game, child);
                    newPopulation[i].copy(child);
                }

                // order by fitness
                orderPopulation(newPopulation);
                for (int i = 0; i < nPop; i++) {
                    population[i].copy(newPopulation[i]);
                }
            } else {
                // simple mu + lambda evolutionary algorithm
                replaceWorstByBest(population, mu, game);
                orderPopulation(population);
            }
        }

        MOVE next = majorityVote(population);
        if (next == null) return getAliveMove(game);
//        MOVE next = population[0].moves[0];
        return next;
	}

	/*
	 *  initialize an ordered population
	 */
	private Population[] init(Game game, int nPop, int seqLen) {
        Population[] p = new Population[nPop];
        for (int i = 0; i < nPop; i++) {
            p[i] = new Population();
            for (int j = 0; j < seqLen; j++) {
                p[i].moves[j] = randomMove();
            }
            p[i].fitness = evaluateFitness(game, p[i]);
        }

        orderPopulation(p);

        return p;
    }

    /*
     *  use priority queue to sort population by fitness in increasing order
     */
    private void orderPopulation(Population[] p) {
        int nPop = p.length;
        PriorityQueue<Population> pq = new PriorityQueue<Population>(nPop,
                new Comparator<Population>() {
                    public int compare(Population p1, Population p2) {
                        return p2.fitness - p1.fitness;
                    }
                });

        for (int i = 0; i < nPop; i++) {
            pq.offer(p[i]);
        }

        for (int i = 0; i < nPop; i++) {
            p[i] = pq.poll();
        }
    }

    /*
     *  generate random move in evolutionary algorithm
     */
    private MOVE randomMove() {
        int rand = new Random().nextInt(4);
        MOVE move = null;
        switch (rand) {
            case 0:
                move = MOVE.RIGHT;
                break;
            case 1:
                move = MOVE.DOWN;
                break;
            case 2:
                move = MOVE.LEFT;
                break;
            case 3:
                move = MOVE.UP;
                break;
            default:
                break;
        }
        return move;
    }

    /*
     *  evaluate fitness of a given move sequence
     */
    private int evaluateFitness(Game game, Population p) {
        MOVE[] moves = p.moves;
        int n = moves.length;
        Game current = game.copy();
        for (int i = 0; i < n; i++) {
            Game nextGame = current.copy();
            while (true) {
                EnumMap<Constants.GHOST, MOVE> ghostMoves = new EnumMap<Constants.GHOST, MOVE>(Constants.GHOST.class);
                for (Constants.GHOST ghost : Constants.GHOST.values()) {
                    int loc = current.getGhostCurrentNodeIndex(ghost);
                    MOVE[] ghostMove = current.getPossibleMoves(loc, current.getGhostLastMoveMade(ghost));
                    if (ghostMove != null && ghostMove.length > 0) {
                        MOVE nextMove = ghostMove[new Random().nextInt(ghostMove.length)];
                        ghostMoves.put(ghost, nextMove);
                    }
                }

                MOVE[] ms = nextGame.getPossibleMoves(nextGame.getPacmanCurrentNodeIndex(), nextGame.getPacmanLastMoveMade());
                boolean possible = false;
                for (MOVE m : ms) {
                    if (m.equals(moves[i])) {
                        possible = true;
                        break;
                    }
                }

                if (!possible) {
                    if (i == 0) return 0;
                    return Math.max((current.getScore() - game.getScore()) * i / 10, 1);
                }

                nextGame = nextGameState(nextGame, moves[i], ghostMoves);
                if (nextGame.wasPacManEaten()) {
                    return 0;
//                    if (i == 0) return 0;
//                    return current.getScore();
                }

                if (!ms[0].equals(moves[i]) || ms.length > 1) {
                    break;
                }
            }
            current = nextGame;
        }

        return Math.max(current.getScore() - game.getScore(), 5);
    }

    /*
     *  randomly select parent from population, ratio proportional to fitness
     */
    private Population select(Population[] populations) {
        int nPop = populations.length;
        int sum = 0;
        for (Population p : populations) {
            sum += p.fitness;
        }

        if (sum == 0) {
            return null;
        }

        double[] ratio = new double[nPop - 1];
        double prevSum = 0.0;
        for (int i = 0; i < nPop - 1; i++) {
            prevSum += populations[i].fitness;
            ratio[i] = prevSum / sum;
        }

        // find which interval the random number is in
        double rand = new Random().nextDouble();
        int i = 0;
        for (i = 0; i < nPop - 1; i++) {
            if (rand < ratio[i])
                break;
        }

        return populations[i];
    }

    /*
     *  reproduce child with crossover
     */
    private Population reproduce(Population father, Population mother) {
        int n = father.moves.length;
        int c = new Random().nextInt(n);
        Population child = new Population();
        for (int i = 0; i < n; i++) {
            if (i <= c) child.moves[i] = father.moves[i];
            else child.moves[i] = mother.moves[i];
        }
        return child;
    }

    /*
     *  chances are child will mutate at some position
     */
    private void mutate(Population p) {
        final double rate = 0.05;
        int n = p.moves.length;
        for (int i = 0; i < n; i++) {
            if (new Random().nextDouble() < rate) {
                MOVE m = randomMove();
                int iter = 5;
                while (m.equals(p.moves[i]) && iter-- >= 0) {
                    m = randomMove();
                }
                p.moves[i] = m;
            }
        }
    }

    /*
     *  replace n worst population by n mutated best population
     */
    private void replaceWorstByBest(Population[] populations, int mu, Game game) {
        int n = populations.length;
        for (int i = 0; i < mu; i++) {
            Population p = new Population();
            p.copy(populations[i]);
            mutate(p);
            p.fitness = evaluateFitness(game, p);
            populations[n-1-i].copy(p);
        }
    }

    /*
     *  use majority vote to decide next move
     */
    private MOVE majorityVote(Population[] populations) {
        int n = populations.length;
        int[] moveCount = new int[4];

        for (Population p : populations) {
            if (p.moves[0].equals(MOVE.RIGHT)) moveCount[0]++;
            else if (p.moves[0].equals(MOVE.DOWN)) moveCount[1]++;
            else if (p.moves[0].equals(MOVE.LEFT)) moveCount[2]++;
            else if (p.moves[0].equals(MOVE.UP)) moveCount[3]++;
        }

        int most = 0;
        int maxCount = moveCount[0];
        for (int i = 1; i < moveCount.length; i++) {
            if (moveCount[i] > maxCount) {
                maxCount = moveCount[i];
                most = i;
            }
        }

        MOVE next = null;
        switch (most) {
            case 0:
                next = MOVE.RIGHT;
                break;
            case 1:
                next = MOVE.DOWN;
                break;
            case 2:
                next = MOVE.LEFT;
                break;
            case 3:
                next = MOVE.UP;
                break;
            default:
                break;
        }
        return next;
    }
}