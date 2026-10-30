WITH samples AS (
    SELECT
        n,
        (ARRAY[
             'Steel Frame', 'Aluminium Panel', 'Plastic Casing',
         'Lithium Battery', 'Industrial Pump', 'Copper Wiring',
         'Glass Container', 'Solar Inverter', 'Cement Block',
         'Textile Fabric'
             ])[((n - 1) % 10) + 1] || ' - Batch ' || n AS product_name,
    (ARRAY[
    'EcoMaterials Ltd', 'GreenTech Industries', 'MetalWorks',
    'Future Energy', 'BuildRight', 'Clean Manufacturing',
    'EcoPack', 'PowerTech', 'Global Components', 'NatureCraft'
    ])[((n - 1) % 10) + 1] AS supplier_name,
    CASE (n - 1) % 4
    WHEN 0 THEN 450.00
    WHEN 1 THEN 1250.00
    WHEN 2 THEN 6500.00
    ELSE 12000.00
END + (n * 7) AS emissions_kg_co2e,
        CASE ((n - 1) / 4) % 4
            WHEN 0 THEN 8.00
            WHEN 1 THEN 18.00
            WHEN 2 THEN 35.00
            ELSE 52.00
END AS uncertainty_percent,
        CASE n % 3
            WHEN 0 THEN 'PENDING'
            WHEN 1 THEN 'APPROVED'
            ELSE 'REJECTED'
END AS status
    FROM generate_series(1, 50) AS n
)
INSERT INTO product_footprints (
    product_name,
    supplier_name,
    emissions_kg_co2e,
    uncertainty_percent,
    status,
    review_comment,
    submitted_at,
    reviewed_at
)
SELECT
    s.product_name,
    s.supplier_name,
    s.emissions_kg_co2e,
    s.uncertainty_percent,
    s.status,
    CASE
        WHEN s.status = 'APPROVED' THEN 'Supplier data verified.'
        WHEN s.status = 'REJECTED' THEN 'Additional documentation required.'
        ELSE NULL
        END,
    CURRENT_TIMESTAMP - s.n * INTERVAL '1 day',
    CASE
    WHEN s.status = 'PENDING' THEN NULL
    ELSE CURRENT_TIMESTAMP - s.n * INTERVAL '1 day' + INTERVAL '2 hours'
END
FROM samples s
WHERE NOT EXISTS (
    SELECT 1
    FROM product_footprints p
    WHERE p.product_name = s.product_name
);
