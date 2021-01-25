package sample;


public class Train {
    private String id;
    private String name;
    private double maxAcceleration;
    private double maxDeceleration;
    private int topSpeed;
    private boolean direction; // true is forwards false is backwards.
    private int speed;
    private int currentTrackId;

    public Train(String id, String name, double maxAcceleration, double maxDeceleration, int topSpeed)
    {
        this.id = id;
        this.name = name;
        this.maxDeceleration = maxDeceleration;
        this.maxAcceleration = maxAcceleration;
        this.topSpeed = topSpeed;
        this.direction = true;
        this.speed = 0;

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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

    public double getmaxDeceleration()
    {
        return maxDeceleration;
    }

    public void setmaxDeceleration(double maxDeceleration)
    {
        this.maxDeceleration = maxDeceleration;
    }

    public double getMaxAcceleration()
    {
        return maxAcceleration;
    }

    public void setMaxAcceleration(double maxAcceleration)
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


