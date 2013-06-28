package by.hzv.patcher.vo

import java.util.Collection;

import groovy.transform.Immutable;


@Immutable
class Patch {
    String patchName;
	PatchType patchType
    Collection<String> queryList
}
