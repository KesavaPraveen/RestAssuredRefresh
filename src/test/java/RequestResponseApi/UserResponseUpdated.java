package RequestResponseApi;

public class UserResponseUpdated {
    private String name;
    private String job;
    private int id;
    private String updatedAt;

    public UserResponseUpdated(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UserResponseUpdated{name='" + name + "', job='" + job + "', id='" +id + "', createdAt='"+updatedAt+"'}";
    }

}
