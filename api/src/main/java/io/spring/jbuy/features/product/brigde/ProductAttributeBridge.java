package io.spring.jbuy.features.product.brigde;

import io.spring.jbuy.features.attribute.Attribute;
import io.spring.jbuy.features.category.Category;
import io.spring.jbuy.features.product_attribute.ProductAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hibernate.search.engine.backend.document.IndexFieldReference;
import org.hibernate.search.mapper.pojo.bridge.PropertyBridge;
import org.hibernate.search.mapper.pojo.bridge.runtime.PropertyBridgeWriteContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")
@RequiredArgsConstructor @Slf4j
public class ProductAttributeBridge implements PropertyBridge<List> {

    private final IndexFieldReference<String> productAttributeField;
    private final IndexFieldReference<String> attributeValueField;
    private static final Set<String> excludeAttributeList = Stream.of(
            "Product Information",
            "Controls",
            "Security",
            "CPU Main Features",
            "Motherboard",
            "Model Number",
            "Pixel Pitch",
            "Horizontal Scan Range",
            "Vertical Scan Range",
            "Native Contrast Ratio",
            "Dynamic Contrast Ratio",
            "Bit Depth",
            "Color Gamut",
            "Built-in Devices",
            "On Screen Display Languages",
            "Environmental Specifications",
            "Environment Specifications",
            "Package Contents",
            "Warranty",
            "Ports",
            "Supported Resolutions",
            "Refresh Rate Details",
            "Color Gamut Details",
            "Box Dimensions (WxDxH)",
            "Viewing Angles",
            "Color Output",
            "Color Support",
            "Power Consumption Operational (Standby)",
            "Voltage Required",
            "Certifications",
            "Viewable Size",
            "Tilt Range",
            "Turning Rate",
            "Height",
            "Width",
            "Depth",
            "Video Card Length"
    ).collect(Collectors.toCollection(HashSet::new));

    @Override
    public void write(DocumentElement target, List bridgedElement, PropertyBridgeWriteContext context) {

        @SuppressWarnings("unchecked")
        List<ProductAttribute> productAttributeList = (List<ProductAttribute>) bridgedElement;

        for (ProductAttribute productAttribute : productAttributeList) {
            Attribute attribute = productAttribute.getAttribute();
            Set<String> categorySet = attribute.getAttributeType()
                    .getListOfCategory()
                    .stream()
                    .map(Category::getName)
                    .collect(Collectors.toSet());
            String attributeType = attribute.getAttributeType().getName();
            String attributeName = attribute.getName();

            if (excludeAttributeList.contains(attributeType) || excludeAttributeList.contains(attributeName)) {
                continue;
            }

            String value = productAttribute.getValue();
            if (!attributeType.contains("Product Information")
                    && !attributeType.contains("Physical Specifications")) {
                if (value.contains("<br>")) {
                    String[] values = value.split("<br>");
                    for (String val : values) {
                        target.addValue(this.attributeValueField, val.trim());
                    }
                } else if (value.contains(",")) {
                    if (value.matches("^([a-zA-Z ]+) \\d+(, \\d+){1,}")) {
                        String word = value.split(" \\d")[0].trim();
                        String[] values = value.split(",");
                        target.addValue(this.attributeValueField, values[0]);
                        for (int i = 1; i < values.length; i++) {
                            target.addValue(this.attributeValueField, word + " " + values[i].trim());
                        }
                    } else {
                        String[] values = value.split(",");
                        for (String val : values) {
                            target.addValue(this.attributeValueField, val.trim());
                        }
                    }
                } else {
                    target.addValue(this.attributeValueField, value);
                }
            }


//            if (attributeName.contains("Weight")) {
//                if (categorySet.contains("Laptops")) {
//                    String strippedValue = value.trim()
//                            .replaceFirst("([\\d.]*)[ ]*(lbs|LBS|pounds|pound|POUND|POUNDS)[.]?.*", "$1");
//                    double weight = Double.parseDouble(strippedValue);
//                    String placement = "|" + attributeType + "__" + attributeName + "__";
//                    if (weight >= 7) {
//                        String weightText = 7 + " lbs or More";
//                        target.addValue(this.productAttributeField,
//                                        weightText + placement + weightText);
//                    } else if (weight >= 6 && weight <= 6.99) {
//                        String weightText = 6 + " to " + 6.9 + " lbs";
//                        target.addValue(this.productAttributeField,
//                                        weightText + placement + weightText);
//                    } else if (weight >= 5 && weight <= 5.99) {
//                        String weightText = 5 + " to " + 5.9 + " lbs";
//                        target.addValue(this.productAttributeField,
//                                        weightText + placement + weightText);
//                    } else if (weight >= 4 && weight <= 4.99) {
//                        String weightText = 4 + " to " + 4.9 + " lbs";
//                        target.addValue(this.productAttributeField,
//                                        weightText + placement + weightText);
//                    } else if (weight >= 3 && weight <= 3.99) {
//                        String weightText = 3 + " to " + 3.9 + " lbs";
//                        target.addValue(this.productAttributeField,
//                                        weightText + placement + weightText);
//                    } else if (weight >= 2 && weight <= 2.99) {
//                        String weightText = 2 + " to " + 49 + " lbs";
//                        target.addValue(this.productAttributeField,
//                                        weightText + placement + weightText);
//                    } else if (weight >= 1 && weight <= 1.99) {
//                        String weightText = 1 + " to " + 1.9 + " lbs";
//                        target.addValue(this.productAttributeField,
//                                        weightText + placement + weightText);
//                    } else {
//                        String weightText = "Less than " + 1 + " lb";
//                        target.addValue(this.productAttributeField,
//                                        weightText + placement + weightText);
//                    }
//                }
//            } else if (attributeName.contains("Screen Size")) {
//                String strippedValue = value
//                        .trim()
//                        .replace("\\", "")
//                        .replace("\"", "");
//                double size = Double.parseDouble(strippedValue);
//                String placement = "|" + attributeType + "__" + attributeName + "__";
//                if (categorySet.contains("TV")) {
//                    if (size >= 60) {
//                        String sizeText = 60 + "\" or More";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 50 && size <= 59.99) {
//                        String sizeText = 50 + "\" to " + 59 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 40 && size <= 49.99) {
//                        String sizeText = 40 + "\" to " + 49 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 30 && size <= 39.99) {
//                        String sizeText = 30 + "\" to " + 39 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 20 && size <= 29.99) {
//                        String sizeText = 20 + "\" to " + 29 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else {
//                        String sizeText = "Less than " + 19 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    }
//                } else if (categorySet.contains("Laptops")
//                        && !(categorySet.contains("Monitors") || categorySet.contains("Desktops"))) {
//                    if (size >= 17) {
//                        String sizeText = 17 + "\" or More";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 16 && size <= 16.9) {
//                        String sizeText = 16 + "\" to " + 16.9 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 15 && size <= 15.9) {
//                        String sizeText = 15 + "\" to " + 15.9 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 14 && size <= 14.9) {
//                        String sizeText = 14 + "\" to " + 14.9 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 13 && size <= 13.9) {
//                        String sizeText = 13 + "\" to " + 13.9 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 12 && size <= 12.9) {
//                        String sizeText = 12 + "\" to " + 12.9 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 11 && size <= 11.9) {
//                        String sizeText = 11 + "\" to " + 11.9 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else {
//                        String sizeText = "Less than " + 11 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    }
//                } else if (categorySet.contains("Monitors") || categorySet.contains("Desktops")) {
//                    if (size >= 50) {
//                        String sizeText = 50 + "\" or More";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 40 && size <= 49.99) {
//                        String sizeText = 40 + "\" to " + 49 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 33 && size <= 39.99) {
//                        String sizeText = 33 + "\" to " + 39 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 27 && size <= 32.99) {
//                        String sizeText = 27 + "\" to " + 32.9 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else if (size >= 23 && size <= 26.99) {
//                        String sizeText = 23 + "\" to " + 26.9 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    } else {
//                        String sizeText = "Less than " + 22.9 + "\"";
//                        target.addValue(this.productAttributeField,
//                                        sizeText + placement + sizeText);
//                    }
//                }
//            }

            if (attributeName.contains("Weight")) {
                continue;
            }

            if (value.contains("\n")) {
                String[] values = value.split("\n");
                for (String token : values) {
                    token = token.trim();
                    String targetValue = attributeName.toLowerCase() + "_" + token.toLowerCase()
                            + "|" + attributeType + "__" + attributeName + "__" + attributeName + "_" + token;
                    target.addValue(this.productAttributeField, targetValue);
                }
            } else if (value.contains(",")) {
                String[] values = value.split(", ");
                for (String token : values) {
                    token = token.trim();
                    String targetValue = attributeName.toLowerCase() + "_" + token.toLowerCase()
                            + "|" + attributeType + "__" + attributeName + "__" + attributeName + "_" + token;
                    target.addValue(this.productAttributeField, targetValue);
                }
            } else if (!value.isBlank()) {
                String targetValue = attributeName.toLowerCase() + "_" + value.toLowerCase()
                        + "|" + attributeType + "__" + attributeName + "__" + attributeName + "_" + value;
                target.addValue(this.productAttributeField, targetValue);
            }

            // syntax: "indexedValue | aggregableValue"
            // example "value.toLowerCase() | attributeType__attributeName__value"
            // indexedValue: value to match when searching
            // aggregableValue: value return from aggregation
            // Note: analyzer "delimiter" in CustomLuceneAnalysisConfigurer ignores text after symbol "|"

        }
    }
}