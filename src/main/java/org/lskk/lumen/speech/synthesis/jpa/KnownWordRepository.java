package org.lskk.lumen.speech.synthesis.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by ceefour on 10/8/15.
 */
public interface KnownWordRepository extends PagingAndSortingRepository<KnownWord, Integer> {

    KnownWord findOneByWord(String word);

}
