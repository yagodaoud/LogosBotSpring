package yagodaoud.com.logos.db.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Guild {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String guildId;

    private String guildName;

    private String guildDescription;

    private int guildMemberCount;

    private String guildIconUrl;

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getGuildDescription() {
        return guildDescription;
    }

    public void setGuildDescription(String guildDescription) {
        this.guildDescription = guildDescription;
    }

    public int getGuildMemberCount() {
        return guildMemberCount;
    }

    public void setGuildMemberCount(int guildMemberCount) {
        this.guildMemberCount = guildMemberCount;
    }

    public String getGuildIconUrl() {
        return guildIconUrl;
    }

    public void setGuildIconUrl(String guildIconUrl) {
        this.guildIconUrl = guildIconUrl;
    }
}
