class Day07(test: Boolean = false): Day<Long>(test, 95437, 24933642) {
    private val rootDir = Directory(mutableMapOf())
    private val allDirs = mutableMapOf<String, Directory>()
    private val rom = 70000000L
    private val needed = 30000000L
    init {
        allDirs["/"] = rootDir
        var currentPath = ""
        var currentDirectory: Directory = rootDir
        val listings = input.joinToString("|")
            .split("|$ ls|")
            .map { it.split("|") }

        listings.forEach { listing ->
            listing.forEach { line ->
                if (line.startsWith("\$ cd")) {
                    val suffix = line.substringAfter("\$ cd ")
                    if (suffix == "..") {
                        currentPath = currentPath.substringBeforeLast("/").substringBeforeLast("/").plus("/")
                        currentDirectory = allDirs[currentPath]!!
                    } else {
                        val oldDirectory = allDirs[currentPath]
                        currentPath += if (suffix != "/") "${suffix}/" else suffix
                        currentDirectory = allDirs.computeIfAbsent(currentPath) { Directory(mutableMapOf())}
                        oldDirectory?.contents?.put(suffix, currentDirectory)
                    }
                } else if (!line.startsWith("dir")) {
                    val match = Regex("(\\d+) (.*)$").find(line)!!.groups.map { it!!.value }
                    val size = match[1].toLong()
                    val name = match[2]
                    currentDirectory.contents[name] = File(size)
                }
            }
        }
    }

    override fun part1(): Long {
        return allDirs.values.filter { it.getSize() <= 100000 }.sumOf { it.getSize() }
    }

    override fun part2(): Long {
        val used = rootDir.getSize()
        val unused = rom - used
        val toFree = needed - unused
        return allDirs.values.filter { it.getSize() >= toFree }.minOf { it.getSize() }
    }
}

sealed interface Sized {
    fun getSize(): Long
}
class File(private val size: Long): Sized {
    override fun getSize(): Long {
        return size
    }
}

class Directory(val contents: MutableMap<String, Sized>): Sized {
    override fun getSize(): Long {
        return contents.values.sumOf { it.getSize() }
    }
}

fun main() {
    Day07().run()
}
