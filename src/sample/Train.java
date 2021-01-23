package sample;


public class Train {
    private int id;
    private String name;
    private int maxDeceleration;
    private int maxAcceleration;
    private int topSpeed;
    private boolean direction; // true is forwards false is backwards.
    private int speed;
    private int currentTrackId;

    public Train(int id, String name, int maxDeceleration, int maxAcceleration, int topSpeed)
    {
        this.id = id;
        this.name = name;
        this.maxDeceleration = maxDeceleration;
        this.maxAcceleration = maxAcceleration;
        this.topSpeed = topSpeed;
        this.direction = true;
        this.speed = 0;

    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getmaxDeceleration()
    {
        return maxDeceleration;
    }

    public void setmaxDeceleration(int maxDeceleration)
    {
        this.maxDeceleration = maxDeceleration;
    }

    public int getMaxAcceleration()
    {
        return maxAcceleration;
    }

    public void setMaxAcceleration(int maxAcceleration)
    {
        this.maxAcceleration = maxAcceleration;
    }

    public int getTopSpeed()
    {
        return topSpeed;
    }

    public void setTopSpeed(int topSpeed)
    {
        this.topSpeed = topSpeed;
    }

    public boolean isDirection()
    {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public int getSpeed()
    {
        return speed;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public int getCurrentTrackId()
    {
        return currentTrackId;
    }

    public void setCurrentTrackId(int currentTrackId)
    {
        this.currentTrackId = currentTrackId;
    }
}


