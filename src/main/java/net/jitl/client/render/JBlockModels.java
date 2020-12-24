package net.jitl.client.render;

import ru.timeconqueror.timecore.api.client.resource.BlockModel;
import ru.timeconqueror.timecore.api.client.resource.BlockModels;
import ru.timeconqueror.timecore.api.client.resource.location.TextureLocation;

import static ru.timeconqueror.timecore.api.client.resource.JSONTimeResource.*;

public class JBlockModels extends BlockModels {

	public static BlockModel cubeOrientableModel(TextureLocation endTexture, TextureLocation sideTexture, TextureLocation frontTexture) {
		String json = object(null, listOf(
				property("parent", "block/orientable"),
				object("textures", listOf(
						property("top", endTexture.toString()),
						property("front", frontTexture.toString()),
						property("side", sideTexture.toString())
				))
		));
		return new BlockModel(json);
	}
}
