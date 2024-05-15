package yagodaoud.com.logos.db;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.db.repository.CommandHistoryRepository;
import yagodaoud.com.logos.db.repository.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DbEventHandlerTest {

    @Mock
    SlashCommandInteractionEvent event;
    @Mock
    User user;
    @Mock
    UserRepository userRepository;
    @Mock
    Guild guild;
    @Mock
    OptionMapping optionMapping;

    @Mock
    CommandHistoryRepository commandHistoryRepository;
    @InjectMocks
    DbEventHandler db;

    @Test
    public void insertDataAsync() {
        when(event.getUser()).thenReturn(user);
        when(event.getGuild()).thenReturn(guild);
        when(user.getIdLong()).thenReturn(1L);

        when(event.getOptions()).thenReturn(Collections.singletonList(optionMapping));
        when(event.getOptions().get(0).getAsString()).thenReturn("option");

        when(userRepository.findByDiscordId(1L)).thenReturn(null, new yagodaoud.com.logos.db.entity.User());

        db.insertDataAsync(event);

        verify(userRepository).save(any());
        verify(commandHistoryRepository).save(any());
    }
}
