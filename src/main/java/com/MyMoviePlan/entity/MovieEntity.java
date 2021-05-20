package com.MyMoviePlan.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "movies")
public class MovieEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String name;

    @Column(length = 1000)
    private String image;

    @Column(name = "bg_image",length = 1000)
    private String bgImage;

    @Column(length = 1000)
    private String story;

    @Column(length = 5)
    private String year;

    @Column(length = 20)
    private String duration;

    @Column(length = 10)
    private String rating;

    @Column(name = "added_on")
    @Temporal(TemporalType.DATE)
    private Date addedOn;

    @Temporal(TemporalType.DATE)
    private Date release;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(targetEntity = LanguageEntity.class, cascade = CascadeType.ALL)
    @JoinTable(name = "movie_languages",
            joinColumns = @JoinColumn(name = "movie_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "language_id", unique = false))
    private List<LanguageEntity> languages;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(targetEntity = GenreEntity.class, cascade = CascadeType.ALL)
    @JoinTable(name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "genre_id", unique = false))
    private List<GenreEntity> genres;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(targetEntity = ActorEntity.class, cascade = CascadeType.ALL, mappedBy = "movie")
    private List<ActorEntity> casts;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(targetEntity = ActorEntity.class, cascade = CascadeType.ALL, mappedBy = "movie")
    private List<ActorEntity> crews;

    public MovieEntity(String name, String image, String bgImage, String story, String year,
                       String duration, String rating, Date addedOn, Date release, List<LanguageEntity> languages,
                       List<GenreEntity> genres, List<ActorEntity> casts, List<ActorEntity> crews) {
        this.name = name;
        this.image = image;
        this.bgImage = bgImage;
        this.story = story;
        this.year = year;
        this.duration = duration;
        this.rating = rating;
        this.addedOn = addedOn;
        this.release = release;
        this.languages = languages;
        this.genres = genres;
        this.casts = casts;
        this.crews = crews;
    }

    public MovieEntity setId(int id) {
        this.id = id;
        return this;
    }

    public MovieEntity setName(String name) {
        this.name = name;
        return this;
    }

    public MovieEntity setImage(String image) {
        this.image = image;
        return this;
    }

    public MovieEntity setBgImage(String bgImage) {
        this.bgImage = bgImage;
        return this;
    }

    public MovieEntity setStory(String story) {
        this.story = story;
        return this;
    }

    public MovieEntity setYear(String year) {
        this.year = year;
        return this;
    }

    public MovieEntity setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public MovieEntity setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public MovieEntity setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
        return this;
    }

    public MovieEntity setRelease(Date release) {
        this.release = release;
        return this;
    }

    public MovieEntity setLanguages(List<LanguageEntity> languages) {
        this.languages = languages;
        return this;
    }

    public MovieEntity setGenres(List<GenreEntity> genres) {
        this.genres = genres;
        return this;
    }

    public MovieEntity setCasts(List<ActorEntity> casts) {
        this.casts = casts;
        return this;
    }

    public MovieEntity setCrews(List<ActorEntity> crews) {
        this.crews = crews;
        return this;
    }
}
