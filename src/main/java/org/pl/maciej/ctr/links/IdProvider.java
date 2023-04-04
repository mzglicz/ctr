package org.pl.maciej.ctr.links;

@FunctionalInterface
public interface IdProvider {
    String getUnique(String target);
}
