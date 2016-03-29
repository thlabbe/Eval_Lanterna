package org.thlabbe.eval.lanterna.datas;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataSetBuilder {

	public DataSetBuilder() {
	}

	public DSProject load(String fileName, String root, String name) {
		DSProject res = new DSProject(root, name);
		try {
			DSRegion currentRegion = null;
			DSJob currentJob = null;
			DSDate currentDate = null;

			List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
			String path = res.getPath();
			for (String line : lines) {
				if (line.startsWith("total")) {
					continue;
				}
				if (line.startsWith(path)) {
					String lineWithoutPath = line.substring(path.length() + 1);
					String[] tab = lineWithoutPath.split("/");
					String strDate = tab[0];
					String strJob = tab[1];
					String strRegion = tab[2];

					currentDate = (DSDate) res.obtainsDSItem(path, strDate);
					currentJob = (DSJob) currentDate.obtainsDSItem(currentDate.getPath(), strJob);
					currentRegion = (DSRegion) currentJob.obtainsDSItem(currentJob.getPath(), strRegion);
					currentJob.addContent(currentRegion);
					currentDate.addContent(currentJob);
					res.addContent(currentDate);
				}
				if (line.startsWith("-")) {
					String[] tab = line.split(" ");
					String leafName = tab[tab.length - 1];
					DSItemLeaf leaf = (DSItemLeaf) currentRegion.obtainsDSItem(currentRegion.getPath(), leafName);
					leaf.setTimestamp(line.substring(33, 45));
					currentRegion.addContent(leaf);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

}
