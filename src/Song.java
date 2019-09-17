public class Song implements Comparable<Song> {
    private String title;
    private String nameAuthor;
    private String nameAlbum;
    private int year;

    public Song() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", nameAuthor='" + nameAuthor + '\'' +
                ", nameAlbum='" + nameAlbum + '\'' +
                ", year=" + year +
                '}';
    }

    @Override
    public int compareTo(Song song) {
        int a= title.compareTo(song.getTitle());
        int b=nameAuthor.compareTo(song.getNameAuthor());
        if(a==b) return 0;
        else return -1;
    }
}
