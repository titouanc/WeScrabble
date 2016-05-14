package be.ititou.wescrabble;

import edu.vub.at.android.util.AssetInstaller;

public class WeScrabbleAssetInstaller extends AssetInstaller {
	public WeScrabbleAssetInstaller() {
		super();
		development = true;
	}

	public WeScrabbleAssetInstaller(boolean defaultAssets) {
		super(defaultAssets);
	}
}
