package se.culvertsoft.mgen.compiler.plugins

import java.io.File
import java.lang.reflect.Modifier
import java.net.URL
import java.net.URLClassLoader

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet
import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.api.plugins.Generator
import se.culvertsoft.mgen.api.plugins.Parser
import se.culvertsoft.mgen.api.util.internal.ListClasses
import se.culvertsoft.mgen.api.util.internal.ListFiles
import se.culvertsoft.mgen.compiler.defaultparser.DefaultParser

object FindPlugins {

  val DEFAULT_PATH = "plugins/"

  def defaultFolderExists(): Boolean = {
    val f = new File(DEFAULT_PATH)
    f.exists() && f.isDirectory()
  }
}

class FindPlugins(pluginPaths_in: Seq[String]) {

  import FindPlugins._

  def getPaths(): Seq[String] = {
    val custom = pluginPaths_in.toList.filter(_.nonEmpty)
    if (defaultFolderExists) {
      DEFAULT_PATH :: custom
    } else {
      custom
    }
  }

  val pluginPaths = getPaths()

  lazy val (parserClasses, generatorClasses) = findPluginClasses()

  private def findPluginClasses(): (HashMap[String, Class[_ <: Parser]], HashMap[String, Class[_ <: Generator]]) = {

    val fileNames = pluginPaths.flatMap(path => ListFiles.recursively(path, ".jar"))
    val classNames = fileNames.flatMap(file => ListClasses.namesInJar(file)).toSet
    val jarUrls = fileNames.map(fileName => new URL("jar:file:" + fileName + "!/")).toArray

    val classLoader = URLClassLoader.newInstance(jarUrls)

    val parss = new HashMap[String, Class[_ <: Parser]]
    val gens = new HashMap[String, Class[_ <: Generator]]

    parss.put(classOf[DefaultParser].getName(), classOf[DefaultParser])

    classNames.map(className => {
      val cls = Class.forName(className, false, classLoader)
      if (!Modifier.isAbstract(cls.getModifiers()))
        if (classOf[Parser].isAssignableFrom(cls)) {
          parss.put(cls.asInstanceOf[Class[Parser]].getName(), cls.asInstanceOf[Class[Parser]])
        } else if (classOf[Generator].isAssignableFrom(cls)) {
          gens.put(cls.asInstanceOf[Class[Generator]].getName(), cls.asInstanceOf[Class[Generator]])
        }
    })

    (parss, gens)

  }

}
