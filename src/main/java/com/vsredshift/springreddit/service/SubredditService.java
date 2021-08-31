package com.vsredshift.springreddit.service;

import com.vsredshift.springreddit.dto.SubredditDto;
import com.vsredshift.springreddit.exception.SubredditNotFoundException;
import com.vsredshift.springreddit.mapper.SubredditMapper;
import com.vsredshift.springreddit.model.Subreddit;
import com.vsredshift.springreddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final SubredditMapper subredditMapper;

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository
                .findById(id)
                .orElseThrow(() -> new SubredditNotFoundException(
                        "Subreddit with id " + id + " not found"
                ));

        return subredditMapper.mapSubredditToDto(subreddit);

    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

}
