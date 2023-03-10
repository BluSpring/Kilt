package net.minecraftforge.registries

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar
import io.github.fabricators_of_create.porting_lib.util.RegistryObject as FabricRegistryObject
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.apache.logging.log4j.LogManager
import xyz.bluspring.kilt.mixin.LazyRegistrarAccessor
import xyz.bluspring.kilt.mixin.porting_lib.RegistryObjectAccessor
import xyz.bluspring.kilt.injections.porting_lib.RegistryObjectInjection
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.function.Supplier

class DeferredRegister<T> private constructor(
    private val fabricRegister: LazyRegistrar<T>
) {
    private val optionalTags = mutableMapOf<TagKey<T>, MutableSet<Supplier<T>>>()
    private val fabricRegisteredList = ConcurrentLinkedQueue<RegistryObject<*>>()

    fun createOptionalTagKey(path: String, defaults: Set<Supplier<T>>): TagKey<T> {
        return createOptionalTagKey(ResourceLocation(fabricRegister.mod_id, path), defaults)
    }

    fun createOptionalTagKey(location: ResourceLocation, defaults: Set<Supplier<T>>): TagKey<T> {
        val tagKey = createTagKey(location)

        addOptionalTagDefaults(tagKey, defaults)
        return tagKey
    }

    fun createTagKey(path: String): TagKey<T> {
        return createTagKey(ResourceLocation(fabricRegister.mod_id, path))
    }

    fun createTagKey(location: ResourceLocation): TagKey<T> {
        return TagKey.create(fabricRegister.registryKey, location)
    }

    fun addOptionalTagDefaults(name: TagKey<T>, defaults: Set<Supplier<T>>) {
        if (!optionalTags.contains(name))
            optionalTags[name] = mutableSetOf()

        optionalTags[name]!!.addAll(defaults)
    }

    fun register(bus: IEventBus) {
        bus.register(EventDispatcher(this))
        bus.addListener(this::createRegistry)
    }

    class EventDispatcher(private val register: DeferredRegister<*>) {
        private val logger = LogManager.getLogger("Kilt Registry")

        @SubscribeEvent
        fun handleEvent(event: RegisterEvent) {
            if (event.registryKey != register.registryKey)
                return

            register(register.fabricRegister)

            if (event.forgeRegistry != null) // let's register you too
                register(event.forgeRegistry.fabricRegistry)

            while (register.fabricRegisteredList.isNotEmpty()) {
                (register.fabricRegisteredList.remove().fabricRegistryObject as RegistryObjectInjection).updateRef()
            }
        }

        private fun <T> register(fabricRegistry: LazyRegistrar<T>) {
            // Recreate LazyRegistrar#register(), to make it also check and make sure
            // the registry doesn't register multiple times. This is to temporarily work around
            // a bug that causes double-registering in Kilt.
            val registry: Registry<T> = fabricRegistry.makeRegistry().get() as Registry<T>
            (fabricRegistry as LazyRegistrarAccessor<T>).entrySet.forEach { (it, value) ->
                println("registering ${it.key}")
                val registryValue = value.get()

                if (registry.containsKey(it.key!!)) {
                    logger.warn("Registry object ${it.key} in ${registry.key()} called for registry twice! This is likely a bug in Kilt itself, and has been ignored.")
                    (it as RegistryObjectAccessor<T>).callSetValue(registryValue)
                    return@forEach
                }

                (it as RegistryObjectAccessor<T>).callSetValue(registryValue)
                Registry.register(registry, it.id, it.get())
            }
        }
    }

    val entries: Collection<RegistryObject<T>>
        get() {
            return fabricRegister.entries.map {
                RegistryObject(it)
            }
        }

    val registryKey: ResourceKey<out Registry<T>>
        get() {
            return fabricRegister.registryKey
        }

    val registryName: ResourceLocation
        get() {
            return fabricRegister.registryKey.location()
        }

    private fun createRegistry(event: NewRegistryEvent) {
        event.create(RegistryBuilder<T>().setName(this.registryName)) {
            val tagManager = it.tags() ?: return@create

            optionalTags.forEach(tagManager::addOptionalTagDefaults)
        }
    }

    fun makeRegistry(sup: Supplier<RegistryBuilder<T>>): Supplier<IForgeRegistry<T>> {
        return Supplier {
            ForgeRegistry(RegistryManager.ACTIVE, this.registryKey.location(), sup.get())
        }
    }

    fun <I : T> register(name: String, sup: Supplier<out I>): RegistryObject<I> {
        return RegistryObject(fabricRegister.register(name, sup))
    }

    // not used by any Forge mods, but is used internally by Kilt due to the existence
    // of Porting Lib.
    fun <I : T> register(name: String): RegistryObject<I> {
        val resourceLocation = ResourceLocation(fabricRegister.mod_id, name)
        val registryObject = RegistryObject<I>(FabricRegistryObject(resourceLocation, ResourceKey.create(fabricRegister.registryKey, resourceLocation)))

        fabricRegisteredList.add(registryObject)

        return registryObject
    }

    companion object {
        @JvmStatic
        fun <B> create(key: ResourceKey<out Registry<B>>, modid: String): DeferredRegister<B> {
            return DeferredRegister(LazyRegistrar.create(key, modid))
        }

        @JvmStatic
        fun <B> create(registryName: ResourceLocation, modid: String): DeferredRegister<B> {
            return DeferredRegister(LazyRegistrar.create(registryName, modid))
        }

        @JvmStatic
        fun <B> create(reg: IForgeRegistry<B>, modid: String): DeferredRegister<B> {
            return DeferredRegister(LazyRegistrar.create(reg.registryKey, modid))
        }

        @JvmStatic
        fun <B> createOptional(key: ResourceKey<out Registry<B>>, modid: String): DeferredRegister<B> {
            return DeferredRegister(LazyRegistrar.create(key, modid))
        }

        @JvmStatic
        fun <B> createOptional(registryName: ResourceLocation, modid: String): DeferredRegister<B> {
            return DeferredRegister(LazyRegistrar.create(registryName, modid))
        }
    }
}