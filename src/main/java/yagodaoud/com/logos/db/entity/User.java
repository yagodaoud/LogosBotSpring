package yagodaoud.com.logos.db.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @GenericGenerator(name = "native")
    private Integer id;

    @Column(nullable = false, unique = true)
    private Long discordId;

    private String guildName;

    private String globalName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(Long discordId) {
        this.discordId = discordId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String name) {
        this.guildName = name;
    }

    public String getGlobalName() {
        return globalName;
    }

    public void setGlobalName(String globalName) {
        this.globalName = globalName;
    }
}