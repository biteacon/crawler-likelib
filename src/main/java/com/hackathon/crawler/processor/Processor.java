package com.hackathon.crawler.processor;

import com.hackathon.crawler.data.entities.Block;
import com.hackathon.crawler.data.repository.BlockRepository;
import com.hackathon.crawler.processor.receivers.IDataReceiver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Processor {

    private final BlockRepository blockRepository;
    private final IDataReceiver dataReceiver;
    private final BlockBuilder blockBuilder;

    public Processor(BlockRepository blockRepository,
                     IDataReceiver dataReceiver,
                     BlockBuilder blockBuilder) {
        this.blockRepository = blockRepository;
        this.dataReceiver = dataReceiver;
        this.blockBuilder = blockBuilder;
    }

    @Transactional
    public void doScript() {

        Block block = blockBuilder.buildBlock("+URypoatHM2ZI9v+0GCNGYH3CQk4OlpP+vBpOrjftw4=");
        //blockRepository.save(block);
        //System.out.println();

    }

}
