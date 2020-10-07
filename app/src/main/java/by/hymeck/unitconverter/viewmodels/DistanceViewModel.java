package by.hymeck.unitconverter.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import by.hymeck.unitconverter.logic.domain.distance.*;
import by.hymeck.unitconverter.logic.domain.distance.units.*;


public class DistanceViewModel extends ViewModel
{
    // TODO: Implement the ViewModel
    private static final String emptyField = "0";

    private final MutableLiveData<String> from = new MutableLiveData<>(emptyField);
    private final MutableLiveData<String> to = new MutableLiveData<>(emptyField);

    private final DistanceConverter distanceConverter = new DistanceConverter();

    private MutableLiveData<DistanceUnits> fromUnit;
    private MutableLiveData<DistanceUnits> toUnit;


    public void input(String digit)
    {
        from.setValue(from.getValue() + digit);
        convert();
    }

    public LiveData<String> getFrom()
    {
        return from;
    }

    public LiveData<String> getTo()
    {
        return to;
    }

    public void setPeriod()
    {
        String value = from.getValue();
        if (!value.contains("."))
        {
            if (value.equals(emptyField))
                from.setValue("0.");

            else
                from.setValue(value + ".");
            // TODO: convert?
        }
    }

    public void erase()
    {
        String value = from.getValue();
        int length = value.length();

        if (length > 1)
        {
            from.setValue(value.substring(0, length - 1));
            convert();
        }

        else
            clear();
    }

    public void clear()
    {
        from.setValue(emptyField);
        to.postValue(emptyField);
    }

    public void convert()
    {
        double value = Double.parseDouble(to.getValue());
        DistanceMetricUnit fromUnit = getDistanceMetricUnit(this.fromUnit.getValue());
        Distance from = new Distance(fromUnit, value);

        DistanceMetricUnit toUnit = getDistanceMetricUnit(this.toUnit.getValue());
        Distance to = distanceConverter.Convert(from, toUnit);

        this.to.postValue(Double.toString(to.value));
    }

    public void setFromUnit(String unit)
    {
        DistanceUnits u = DistanceUnits.valueOf(unit);
        if (u == fromUnit.getValue())
            return;

        fromUnit.postValue(u);

        DistanceMetricUnit distanceMetricUnit = getDistanceMetricUnit(u);
        double value = Double.parseDouble(from.getValue());
        Distance from = new Distance(distanceMetricUnit, value);

        distanceMetricUnit = getDistanceMetricUnit(toUnit.getValue());
        Distance to = distanceConverter.Convert(from, distanceMetricUnit);
        this.to.setValue(Double.toString(to.value));
    }

    public void setToUnit(String unit)
    {
        DistanceUnits u = DistanceUnits.valueOf(unit);
        if (u == toUnit.getValue())
            return;

        toUnit.postValue(u);

        double fromValue = Double.parseDouble(from.getValue());
        DistanceMetricUnit distanceMetricUnit = getDistanceMetricUnit(fromUnit.getValue());
        Distance from = new Distance(distanceMetricUnit, fromValue);

        double value = Double.parseDouble(to.getValue());

        DistanceMetricUnit convertedUnit = getDistanceMetricUnit(u);
        Distance to = distanceConverter.Convert(from, convertedUnit);

        this.to.setValue(Double.toString(to.value));
    }

    private DistanceMetricUnit getDistanceMetricUnit(DistanceUnits distanceUnits)
    {
        DistanceMetricUnit distanceMetricUnit;

        switch (distanceUnits)
        {

            case Centimeter:
                distanceMetricUnit = new CentimeterUnit();
                break;
            case Meter:
                distanceMetricUnit = new MeterUnit();
                break;
            case Kilometer:
                distanceMetricUnit = new KilometerUnit();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + distanceUnits);
        }

        return distanceMetricUnit;
    }
}