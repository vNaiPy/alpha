package com.naipy.alpha.modules.token;

import com.naipy.alpha.modules.user.models.User;
import com.naipy.alpha.modules.utils.UniversalSerialVersion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_token")
public class Token implements Serializable {

    @Serial
    private static final long serialVersionUID = UniversalSerialVersion.TOKEN_SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean revoked;

    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}
