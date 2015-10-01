package net.ilexiconn.llibrary.common.animation;

import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.common.message.MessageLLibraryIntemittentAnimation;
import net.minecraft.entity.Entity;

import java.util.Random;

/**
 * This is a timer that can be used to easily animate models with intermittent poses. You have to set the
 * number of ticks between poses, a number of ticks that represents the interval of the pose change, increase
 * or decrease the timer, and get the percentage using a specific function.
 *
 * @author RafaMv
 * @author Paul Fulham
 * @since 0.1.1
 */

public class IntermittentAnimation<T extends Entity & IntermittentAnimatableEntity> extends ControlledAnimation
{
    /**
     * It is the random used to randomize the movement.
     */
    private Random rand = new Random();

    private T entity;

    /**
     * It is a boolean that shows if the animation is already in the new pose.
     */
    private boolean isRunning;

    /**
     * It is the timer used for the interval.
     */
    private int timeIdle;
    /**
     * It is the interval to return to the first animation.
     */
    private int minIdleTime;
    /**
     * It is the chance to go to the new animation.
     */
    private int startProbability;

    private boolean isOperator;

    private byte id;

    /**
     * 
     * @param id id of this intermittent animation
     * @param entity entity that uses this intermittent animation
     * @param duration duration
     * @param intervalDuration minium ticks between animation cycles
     * @param startPropbability propbablity that the animation will begain, higher values have lower probability
     * @param isOperator If true this intermittent animation will run logic for starting animation,
     * If you want this intermittent animation to be syncronized between the server and the client
     * contruct this intermittent animation with isOperator = !entity.worldObj.isRemote, of client only
     * then isOperator should be true
     */
    public IntermittentAnimation(int id, T entity, int duration, int intervalDuration, int startPropbability, boolean isOperator)
    {
        super(duration);
        this.id = (byte) id;
        this.entity = entity;
        this.minIdleTime = intervalDuration;
        this.startProbability = startPropbability;
        this.isOperator = isOperator;
        isRunning = false;
        timerChange = -1;
    }

    public byte getId()
    {
        return id;
    }

    /**
     * Sets the timer to a specific value.
     *
     * @param timeRunning
     *            is the number of ticks to be set.
     */
    public void setTimeRunning(int timeRunning)
    {
        this.timer = timeRunning;

        if (this.timer > duration)
        {
            this.timer = duration;
        }
        else if (this.timer < 0)
        {
            this.timer = 0;
        }
    }

    /**
     * Increases the timer by 1.
     */
    @Override
    public void update()
    {
        super.update();
        if (isRunning)
        {
            if (timer < duration && timer > 0)
            {
                timer += timerChange;
            }
            else
            {
                if (timer >= duration)
                {
                    timer = duration;
                }
                else if (timer <= 0)
                {
                    timer = 0;
                }
                timeIdle = 0;
                isRunning = false;
            }
        }
        else if (isOperator)
        {
            if (timeIdle < minIdleTime)
            {
                timeIdle++;
            }
            else
            {
                if (rand.nextInt(startProbability) == 0)
                {
                    start();
                    LLibrary.networkWrapper.sendToDimension(new MessageLLibraryIntemittentAnimation(entity, id), entity.dimension);
                }
            }
        }
    }

    public void start()
    {
        timerChange = -timerChange;
        timer += timerChange;
        isRunning = true;
    }

    /**
     * Decreases the timer by 1.
     */
    public void stop()
    {
        if (timer > 0)
        {
            timer--;
        }
        else
        {
            timer = 0;
            isRunning = false;
            timeIdle = 0;
            timerChange = 1;
        }
    }

    /**
     * Decreases the timer by a specific value.
     *
     * @param timeDelta
     *            is the number of ticks to be decreased in the timer
     */
    public void stop(int timeDelta)
    {
        if (timer - timeDelta > 0)
        {
            timer -= timeDelta;
        }
        else
        {
            timer = 0;
            isRunning = false;
            timeIdle = 0;
            timerChange = 1;
        }
    }
}
