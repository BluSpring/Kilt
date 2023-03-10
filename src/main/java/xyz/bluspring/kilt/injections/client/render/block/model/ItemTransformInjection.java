package xyz.bluspring.kilt.injections.client.render.block.model;

import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.ItemTransform;

public interface ItemTransformInjection {
    static ItemTransform create(Vector3f vector3f, Vector3f vector3f2, Vector3f vector3f3, Vector3f rightRotation) {
        var itemTransform = new ItemTransform(vector3f, vector3f2, vector3f3);
        ((ItemTransformInjection) itemTransform).setRightRotation(rightRotation);

        return itemTransform;
    }

    default void setRightRotation(Vector3f rightRotation) {
        throw new IllegalStateException();
    }
}
