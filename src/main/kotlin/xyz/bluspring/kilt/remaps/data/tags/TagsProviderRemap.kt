package xyz.bluspring.kilt.remaps.data.tags

import net.minecraft.core.Registry
import net.minecraft.data.DataGenerator
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.tags.TagManager
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.common.data.ExistingFileHelper.ResourceType
import java.nio.file.Path

abstract class TagsProviderRemap<T>(dataGenerator: DataGenerator, registry: Registry<T>, protected val modId: String, protected val existingFileHelper: ExistingFileHelper) : TagsProvider<T>(dataGenerator, registry) {
    private val resourceType = ResourceType(PackType.SERVER_DATA, ".json", TagManager.getTagDir(registry.key()))
    private val elementResourceType = ResourceType(PackType.SERVER_DATA, ".json", ForgeHooks.prefixNamespace(registry.key().location()))

    protected fun getPath(id: ResourceLocation): Path? {
        return pathProvider.json(id)
    }
}