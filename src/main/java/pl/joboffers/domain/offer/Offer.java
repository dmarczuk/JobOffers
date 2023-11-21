package pl.joboffers.domain.offer;

import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
class Offer {

    private Integer id;
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) && Objects.equals(url, offer.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }
}
