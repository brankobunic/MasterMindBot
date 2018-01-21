package Utils;
import java.util.Random;

import com.tiggerbiggo.prima.core.Vector2;
import com.tiggerbiggo.prima.exception.IllegalMapSizeException;
import com.tiggerbiggo.prima.processing.fragment.Fragment;

public class NoiseGenerator implements Fragment<Vector2>{
	
	Random rand = new Random();
	private Vector2 xy = new Vector2(rand.nextDouble() * 50.0, rand.nextDouble() * 50.0);;
	

	@Override
	public Fragment<Vector2>[][] build(Vector2 arg0) throws IllegalMapSizeException {
		
		NoiseGenerator[][] map = new NoiseGenerator[arg0.iX()][arg0.iY()];
		
		for(int i = 0; i < arg0.iX(); i++) {
			for(int j = 0; j < arg0.iY(); j++) {
				map[i][j] = new NoiseGenerator();
			}
		}
		
		return map;
	}

	@Override
	public Vector2 get() {
		return this.xy;
	}

	
	
}
