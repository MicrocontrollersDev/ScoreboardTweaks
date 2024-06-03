plugins {
    alias(libs.plugins.pgt.root)
    alias(libs.plugins.kotlin) apply false
}

preprocess {
    val fabric12006 = createNode("1.20.6-fabric", 12006, "yarn")
    val fabric12004 = createNode("1.20.4-fabric", 12004, "yarn")
    val fabric12001 = createNode("1.20.1-fabric", 12001, "yarn")

    fabric12004.link(fabric12006)
    fabric12001.link(fabric12004)
}
