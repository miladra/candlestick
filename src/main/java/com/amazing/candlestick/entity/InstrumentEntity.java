package com.amazing.candlestick.entity;

import com.amazing.candlestick.dto.MessageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity(name = "instrument")
@Getter
@Setter
public class InstrumentEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int Id;

    private String isin;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String description;

    @Enumerated(EnumType.STRING)
    private MessageType type;


    @OneToMany(mappedBy = "instrument", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<QuoteEntity> quotes;
}

