package com.produck.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.produck.domain.enumeration.JoinerRole;
import com.produck.domain.enumeration.TransactionType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SplitBookDetail.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "split_book_detail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplitBookDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description")
    private String description;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "group_id")
    private String groupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "joiner_role")
    private JoinerRole joinerRole;

    @JsonIgnoreProperties(value = { "splitBook", "splitBookDetail" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private SplitBookJoiner splitBookJoiner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "splitBookDetails", "splitBookJoiners", "user" }, allowSetters = true)
    private SplitBook splitBook;
}
