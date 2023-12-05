fun main() {
    data class MapLine(val dst: Long, val src: Long, val size: Long) {
        fun get(src: Long): Long? {
            if (src >= this.src && src < this.src + size) {
                return dst + src - this.src
            }
            return null
        }
    }

    data class Mapping(private val mapLines: List<MapLine>, private val nextMapping: Mapping?) {
        fun get(src: Long): Long {
            val dst = mapLines.asSequence().map { it.get(src) }.find { it != null } ?: src

            return nextMapping?.get(dst) ?: dst
        }
    }

    fun toSeeds(line: String): List<Long> {
        val colonPos = line.indexOf(':')
        return line.substring(colonPos + 1).split(' ').filter { it.isNotEmpty() }.map { it.toLong() }
    }

    fun toSeedsFromRanges(line: String): Sequence<Long> {
        val seeds = toSeeds(line)
        return seeds.asSequence().windowed(2, 2).flatMap {
            it[0] until (it[0] + it[1])
        }
    }

    fun toMapLines(lines: List<String>): List<MapLine> {
        return lines.map {
            val split = it.split(' ').map { nr -> nr.toLong() }
            MapLine(split[0], split[1], split[2])
        }
    }

    fun buildMapping(lines: List<String>): Mapping {
        val map1Idx = lines.indexOf("seed-to-soil map:") + 1
        val map2Idx = lines.indexOf("soil-to-fertilizer map:") + 1
        val map3Idx = lines.indexOf("fertilizer-to-water map:") + 1
        val map4Idx = lines.indexOf("water-to-light map:") + 1
        val map5Idx = lines.indexOf("light-to-temperature map:") + 1
        val map6Idx = lines.indexOf("temperature-to-humidity map:") + 1
        val map7Idx = lines.indexOf("humidity-to-location map:") + 1

        val mapLines1 = toMapLines(lines.subList(map1Idx, map2Idx - 2))
        val mapLines2 = toMapLines(lines.subList(map2Idx, map3Idx - 2))
        val mapLines3 = toMapLines(lines.subList(map3Idx, map4Idx - 2))
        val mapLines4 = toMapLines(lines.subList(map4Idx, map5Idx - 2))
        val mapLines5 = toMapLines(lines.subList(map5Idx, map6Idx - 2))
        val mapLines6 = toMapLines(lines.subList(map6Idx, map7Idx - 2))
        val mapLines7 = toMapLines(lines.subList(map7Idx, lines.size))

        val mapping7 = Mapping(mapLines7, null)
        val mapping6 = Mapping(mapLines6, mapping7)
        val mapping5 = Mapping(mapLines5, mapping6)
        val mapping4 = Mapping(mapLines4, mapping5)
        val mapping3 = Mapping(mapLines3, mapping4)
        val mapping2 = Mapping(mapLines2, mapping3)
        val mapping1 = Mapping(mapLines1, mapping2)
        return mapping1
    }

    fun part1(lines: List<String>): Long {
        val seeds = toSeeds(lines[0])
        val mapping1 = buildMapping(lines)

        val lowest = seeds.map {
            mapping1.get(it)
        }.min()

        return lowest
    }

    fun part2(lines: List<String>): Long {
        val seeds = toSeedsFromRanges(lines[0])
        val mapping1 = buildMapping(lines)

        var lowest: Long? = null

        for ((idx, seed) in seeds.withIndex()) {
            if (idx % 1000 == 0) {
                println("Busy with seed $idx")
            }

            val location = mapping1.get(seed)
            if (lowest == null || location < lowest) {
                lowest = location
            }
        }

        return lowest!!
    }

    val lines = readLines("Day05.txt")

    part1(lines).println()
    part2(lines).println()
}
