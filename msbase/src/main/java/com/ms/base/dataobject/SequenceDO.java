package com.ms.base.dataobject;

public class SequenceDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sequence_record.name
     *
     * @mbg.generated Sat Mar 21 18:40:03 CST 2020
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sequence_record.current_value
     *
     * @mbg.generated Sat Mar 21 18:40:03 CST 2020
     */
    private Integer currentValue;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sequence_record.step
     *
     * @mbg.generated Sat Mar 21 18:40:03 CST 2020
     */
    private Integer step;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sequence_record.name
     *
     * @return the value of sequence_record.name
     *
     * @mbg.generated Sat Mar 21 18:40:03 CST 2020
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sequence_record.name
     *
     * @param name the value for sequence_record.name
     *
     * @mbg.generated Sat Mar 21 18:40:03 CST 2020
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sequence_record.current_value
     *
     * @return the value of sequence_record.current_value
     *
     * @mbg.generated Sat Mar 21 18:40:03 CST 2020
     */
    public Integer getCurrentValue() {
        return currentValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sequence_record.current_value
     *
     * @param currentValue the value for sequence_record.current_value
     *
     * @mbg.generated Sat Mar 21 18:40:03 CST 2020
     */
    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sequence_record.step
     *
     * @return the value of sequence_record.step
     *
     * @mbg.generated Sat Mar 21 18:40:03 CST 2020
     */
    public Integer getStep() {
        return step;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sequence_record.step
     *
     * @param step the value for sequence_record.step
     *
     * @mbg.generated Sat Mar 21 18:40:03 CST 2020
     */
    public void setStep(Integer step) {
        this.step = step;
    }
}