package yagodaoud.com.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yagodaoud.com.logos.db.repository.CommandHistoryRepository;

@Service
public class CommandHistoryService {

    @Autowired
    private CommandHistoryRepository commandHistoryRepository;

    public long getTotalCommands() {
        return commandHistoryRepository.count();
    }
}
