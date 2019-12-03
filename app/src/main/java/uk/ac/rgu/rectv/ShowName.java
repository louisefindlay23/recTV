package uk.ac.rgu.rectv;

public class ShowName {
    private int episodeNumber;
    private String episodeName;

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    @Override
    public String toString() {
        return "ShowName{" +
                "EpisodeNumber=" + episodeNumber + '\'' +
                ", EpisodeName=" + episodeName +
                '}';
    }
}