package board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
    public final Map<Integer, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();
    public static final int START_TILE_INDEX = 0;
    public static final int NUM_TILES_PER_ROW = 10;
    public static final int NUM_TILES_PER_COL = 10;
    public static final int NUM_TILES = 100;
    
    private Map<Integer, Integer> initializePositionToCoordinateMap() {
        final Map<Integer, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES_PER_ROW; i++) {
        	for(int j = START_TILE_INDEX; j< NUM_TILES_PER_COL; j++) {
                positionToCoordinate.put(i, j);
        	}
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }
}
