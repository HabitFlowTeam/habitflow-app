-- =======================================================
-- FIX: STREAK INCREMENT/DECREMENT LOGIC
-- Problem: Unchecking habits doesn't decrease streak
-- =======================================================

-- SOLUTION 1: Fix the SQL trigger to handle decrement
DROP TRIGGER IF EXISTS smart_streak_update_trigger ON habits_tracking;

CREATE OR REPLACE FUNCTION smart_update_habit_streak_fixed()
RETURNS TRIGGER AS $$
DECLARE
    current_streak INTEGER;
    today_date DATE := CURRENT_DATE;
    habit_scheduled_today BOOLEAN;
    current_day_name TEXT;
    was_checked_before BOOLEAN := FALSE;
BEGIN
    -- Only process for today's date
    IF NEW.tracking_date != today_date THEN
        RETURN NEW;
    END IF;
    
    -- Get current day name
    current_day_name := CASE EXTRACT(DOW FROM NEW.tracking_date)
        WHEN 1 THEN 'Lunes'
        WHEN 2 THEN 'Martes'
        WHEN 3 THEN 'Miércoles'
        WHEN 4 THEN 'Jueves'
        WHEN 5 THEN 'Viernes'
        WHEN 6 THEN 'Sábado'
        WHEN 0 THEN 'Domingo'
    END;
    
    -- Check if habit is scheduled for today
    SELECT EXISTS (
        SELECT 1 
        FROM habits_days hd
        JOIN week_days wd ON hd.week_day_id = wd.id
        WHERE hd.habit_id = NEW.habit_id 
        AND wd.name = current_day_name
    ) INTO habit_scheduled_today;
    
    -- Only process if habit is scheduled for today
    IF NOT habit_scheduled_today THEN
        RETURN NEW;
    END IF;
    
    -- Get current streak
    SELECT streak INTO current_streak
    FROM habits
    WHERE id = NEW.habit_id;
    
    -- Handle different trigger operations
    IF TG_OP = 'INSERT' THEN
        -- New record for today
        IF NEW.is_checked THEN
            -- Marked as completed: increment streak
            UPDATE habits 
            SET streak = current_streak + 1 
            WHERE id = NEW.habit_id;
            
            RAISE NOTICE 'INSERT: Incremented streak for habit % from % to %', 
                NEW.habit_id, current_streak, current_streak + 1;
        END IF;
        -- If inserted as unchecked, keep current streak
        
    ELSIF TG_OP = 'UPDATE' AND OLD.is_checked != NEW.is_checked THEN
        -- Status changed for today
        IF NEW.is_checked AND NOT OLD.is_checked THEN
            -- Changed from unchecked to checked: increment
            UPDATE habits 
            SET streak = current_streak + 1 
            WHERE id = NEW.habit_id;
            
            RAISE NOTICE 'UPDATE: Incremented streak for habit % from % to %', 
                NEW.habit_id, current_streak, current_streak + 1;
                
        ELSIF NOT NEW.is_checked AND OLD.is_checked THEN
            -- Changed from checked to unchecked: decrement (but minimum 0)
            UPDATE habits 
            SET streak = GREATEST(0, current_streak - 1)
            WHERE id = NEW.habit_id;
            
            RAISE NOTICE 'UPDATE: Decremented streak for habit % from % to %', 
                NEW.habit_id, current_streak, GREATEST(0, current_streak - 1);
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the fixed trigger
CREATE TRIGGER smart_streak_update_trigger_fixed
    AFTER INSERT OR UPDATE ON habits_tracking
    FOR EACH ROW
    EXECUTE FUNCTION smart_update_habit_streak_fixed();

