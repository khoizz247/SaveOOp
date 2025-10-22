package GameLoop;

import GameObject.ManageBall;
import GameObject.ManageGameBlock;
import GameObject.MyBlock;
import GameObject.MainCharacter;
import GameObject.Map;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class ScenePlayGame {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private AnimationTimer gameLoop;

    private boolean running = true;
    private int level = 1;
    boolean isIngame = false;

    private MyBlock myBlock;
    private ManageGameBlock listBlocks;
    private ManageBall listBalls;
    private MainCharacter mainCharacter;
    private Map map;

    public void runGame(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        canvas.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));

        initObject();

        startLevel(gc, canvas);
    }

    private void initObject() {
        myBlock = new MyBlock(70, 70, 4);
        mainCharacter = new MainCharacter();
        map = new Map(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());
        listBlocks = new ManageGameBlock();
        listBalls = new ManageBall(myBlock.getY());
    }

    //Vong lap chinh
    private void startLevel(GraphicsContext gc, Canvas canvas) {
        listBlocks.updateList(level);
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (running) {
                    if (isIngame) {
                        updateInGame();
                        renderInGame(gc, canvas);
                        if (listBlocks.getNumberBlock() == 0) {
                            gameLoop.stop();
                            level++;
                            startLevel(gc, canvas);
                        }
                    } else {
                        updateInLoppy();
                        renderInLoppy(gc, canvas);
                    }
                }
            }
        };
        gameLoop.start();
    }

    //Xu li di chuyen cua gach
    private void updateInGame() {
        if (pressedKeys.contains(KeyCode.LEFT)) {
            myBlock.setX(myBlock.getX() - myBlock.getSpeed());
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            myBlock.setX(myBlock.getX() + myBlock.getSpeed());
        }
        myBlock.collisionHandling();
    }

    //Xu li di chuyen nhan vat
    private void updateInLoppy() {

        boolean isW = pressedKeys.contains(KeyCode.W);
        boolean isA = pressedKeys.contains(KeyCode.A);
        boolean isS = pressedKeys.contains(KeyCode.S);
        boolean isD = pressedKeys.contains(KeyCode.D);

        if (!(isA || isD || isW || isS) || (isA && isD && isW && isS)
                || (isA && isD && !isS && !isW) || (!isA && !isD && isS && isW)) {
            mainCharacter.setRunning(false);
        } else {
            mainCharacter.setRunning(true);
            if (isA && !isD) {
                mainCharacter.setDirection(1);
                if (isW && isS || !isW && !isS) {
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() - mainCharacter.getSpeed());
                } else if (isW) {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() - mainCharacter.getSpeed() * Math.sin(45));
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() - mainCharacter.getSpeed() * Math.sin(45));
                } else {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() + mainCharacter.getSpeed() * Math.sin(45));
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() - mainCharacter.getSpeed() * Math.sin(45));
                }
            } else if (isD && !isA) {
                mainCharacter.setDirection(2);
                if (isW && isS || !isW && !isS) {
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() + mainCharacter.getSpeed());
                } else if (isW) {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() - mainCharacter.getSpeed() * Math.sin(45));
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() + mainCharacter.getSpeed() * Math.sin(45));
                } else {
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() + mainCharacter.getSpeed() * Math.sin(45));
                    mainCharacter.setxOnMap(mainCharacter.getxOnMap() + mainCharacter.getSpeed() * Math.sin(45));
                }
            } else {
                if (isW) {
                    mainCharacter.setDirection(3);
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() - mainCharacter.getSpeed());
                } else {
                    mainCharacter.setDirection(0);
                    mainCharacter.setyOnMap(mainCharacter.getyOnMap() + mainCharacter.getSpeed());
                }
            }
        }

        map.setXYOnScreen(mainCharacter.getxOnMap(), mainCharacter.getyOnMap(), mainCharacter.getSize());

        if (pressedKeys.contains(KeyCode.ENTER)) {
            isIngame = true;
        }
    }

    public void pauseGame() {
        running = false;
    }

    public void resumeGame() {
        running = true;
    }

    //Render man hinh game
    private void renderInGame(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        myBlock.addOnScene(gc);
        listBlocks.addListOnScene(gc);
        listBalls.addListOnScene(gc, myBlock, listBlocks.getGameBlocks());
    }

    //Render man hinh sanh
    private void renderInLoppy(GraphicsContext gc, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        map.addMapOnScreen(gc);
        mainCharacter.addCharacterOnScreen(gc);

    }
}
