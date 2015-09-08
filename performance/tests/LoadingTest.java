package tests;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import builder.GameLoader;


public class LoadingTest {

	public static void main(String[] args) {
		int threadNumber = Integer.parseInt(args[0]);
		GameLoader g = new GameLoader(threadNumber);
		long t1 = System.currentTimeMillis();
		g.loadGames("games");
		long t2 = System.currentTimeMillis();
		long executionTime = t2 - t1;
		System.out.println("loaded " + g.getGames().size() + " games in " + (t2 - t1) + " ms");
		
		File f = new File("performance/results/runtimes" + threadNumber);
		try {
			if(!f.exists()){
				f.createNewFile();
			}
			
			FileWriter fw = new FileWriter(f, true);
			fw.append(Long.toString(executionTime));
			fw.append(' ');
			fw.close();
			
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);
			long total = 0;
			int runs = 0;
			while(st.hasMoreTokens()){
				total += Long.parseLong(st.nextToken());
				runs++;
			}
			br.close();
			fr.close();
			
			
			long average = total/runs;
			fw = new FileWriter("performance/results/runtime_average" + threadNumber);
			fw.write(Long.toString(average));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
