import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import '../providers/property_provider.dart';
import '../theme/app_theme.dart';

class CalculatorScreen extends StatefulWidget {
  const CalculatorScreen({super.key});

  @override
  State<CalculatorScreen> createState() => _CalculatorScreenState();
}

class _CalculatorScreenState extends State<CalculatorScreen> {
  double price = 1500000;
  double downPaymentPercent = 10;
  int years = 20;
  double annualRate = 4.5;

  @override
  Widget build(BuildContext context) {
    final provider = context.read<PropertyProvider>();
    final result = provider.calculateMortgage(
      price: price,
      downPaymentPercent: downPaymentPercent,
      years: years,
      ratePercent: annualRate,
    );
    final formatter = NumberFormat('#,###', 'ar');

    return Scaffold(
      appBar: AppBar(
        title: const Row(
          children: [
            Icon(Icons.calculate, color: AppTheme.goldAccent),
            SizedBox(width: 8),
            Text("حاسبة التمويل العقاري"),
          ],
        ),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // كارت النتيجة المميز
          Card(
            color: AppTheme.darkSurfaceVariant,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(20),
              side: const BorderSide(color: AppTheme.goldAccent, width: 0.8),
            ),
            child: Padding(
              padding: const EdgeInsets.all(22),
              child: Column(
                children: [
                  const Text(
                    "القسط الشهري التقديري",
                    style: TextStyle(color: AppTheme.textSecondary, fontSize: 14),
                  ),
                  const SizedBox(height: 8),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.baseline,
                    textBaseline: TextBaseline.alphabetic,
                    children: [
                      Text(
                        formatter.format(result['monthly']),
                        style: const TextStyle(
                          color: AppTheme.goldAccent,
                          fontSize: 34,
                          fontWeight: FontWeight.w900,
                        ),
                      ),
                      const SizedBox(width: 6),
                      const Text(
                        "ر.س / شهر",
                        style: TextStyle(color: AppTheme.textSecondary, fontSize: 14),
                      ),
                    ],
                  ),
                  const SizedBox(height: 18),
                  const Divider(color: AppTheme.darkOutline),
                  const SizedBox(height: 12),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      _summaryCol("مبلغ التمويل", "${formatter.format(result['principal'])} ر.س"),
                      _summaryCol("الدفعة الأولى", "${formatter.format(result['downPayment'])} ر.س"),
                      _summaryCol("تكلفة الأرباح", "${formatter.format(result['totalInterest'])} ر.س"),
                    ],
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 16),

          // سعر العقار
          _controlCard(
            title: "سعر العقار",
            valueText: "${formatter.format(price)} ر.س",
            child: Column(
              children: [
                Slider(
                  value: price,
                  min: 300000,
                  max: 10000000,
                  divisions: 97,
                  activeColor: AppTheme.goldAccent,
                  inactiveColor: AppTheme.darkOutline,
                  onChanged: (val) => setState(() => price = val),
                ),
                Wrap(
                  spacing: 8,
                  children: [
                    _presetChip(500000, "500 ألف"),
                    _presetChip(1000000, "1 مليون"),
                    _presetChip(2000000, "2 مليون"),
                    _presetChip(3500000, "3.5 مليون"),
                  ],
                ),
              ],
            ),
          ),

          // نسبة الدفعة الأولى
          _controlCard(
            title: "نسبة الدفعة الأولى",
            valueText: "${downPaymentPercent.toInt()}% (${formatter.format(result['downPayment'])} ر.س)",
            child: Slider(
              value: downPaymentPercent,
              min: 5,
              max: 50,
              divisions: 9,
              activeColor: AppTheme.goldAccent,
              inactiveColor: AppTheme.darkOutline,
              onChanged: (val) => setState(() => downPaymentPercent = val),
            ),
          ),

          // مدة التمويل بالسنوات
          _controlCard(
            title: "مدة التمويل",
            valueText: "$years سنة (${years * 12} شهر)",
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [10, 15, 20, 25, 30].map((y) {
                final isSelected = years == y;
                return ChoiceChip(
                  label: Text("$y سنة"),
                  selected: isSelected,
                  selectedColor: AppTheme.goldAccent,
                  backgroundColor: AppTheme.darkSurfaceVariant,
                  labelStyle: TextStyle(
                    color: isSelected ? AppTheme.primaryOnColor : AppTheme.textMain,
                    fontWeight: FontWeight.bold,
                    fontSize: 12,
                  ),
                  onSelected: (_) => setState(() => years = y),
                );
              }).toList(),
            ),
          ),

          // نسبة الربح السنوية
          _controlCard(
            title: "نسبة الربح السنوية التقديرية (APR)",
            valueText: "${annualRate.toStringAsFixed(1)}%",
            child: Slider(
              value: annualRate,
              min: 2.5,
              max: 8.0,
              divisions: 11,
              activeColor: AppTheme.goldAccent,
              inactiveColor: AppTheme.darkOutline,
              onChanged: (val) => setState(() => annualRate = val),
            ),
          ),

          // إخلاء مسؤولية
          Container(
            padding: const EdgeInsets.all(14),
            decoration: BoxDecoration(
              color: AppTheme.darkSurfaceVariant,
              borderRadius: BorderRadius.circular(14),
              border: Border.all(color: AppTheme.darkOutline),
            ),
            child: const Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Icon(Icons.info_outline, size: 20, color: AppTheme.goldAccent),
                SizedBox(width: 10),
                Expanded(
                  child: Text(
                    "الحسابات الواردة أعلاه استرشادية فقط وقد تختلف نسبة الربح والقسط الفعلي بناءً على راتب المستفيد والسياسات الائتمانية للبنوك وشركات التمويل المعتمدة في المملكة.",
                    style: TextStyle(color: AppTheme.textSecondary, fontSize: 12, height: 1.5),
                  ),
                ),
              ],
            ),
          ),
          const SizedBox(height: 24),
        ],
      ),
    );
  }

  Widget _presetChip(double p, String label) {
    return ActionChip(
      label: Text(label, style: const TextStyle(fontSize: 11)),
      backgroundColor: AppTheme.darkSurfaceVariant,
      side: const BorderSide(color: AppTheme.darkOutline),
      onPressed: () => setState(() => price = p),
    );
  }

  Widget _summaryCol(String title, String val) {
    return Column(
      children: [
        Text(title, style: const TextStyle(fontSize: 12, color: AppTheme.textSecondary)),
        const SizedBox(height: 4),
        Text(val, style: const TextStyle(fontSize: 13, fontWeight: FontWeight.bold, color: AppTheme.textMain)),
      ],
    );
  }

  Widget _controlCard({
    required String title,
    required String valueText,
    required Widget child,
  }) {
    return Card(
      margin: const EdgeInsets.only(bottom: 14),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(title, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                Text(
                  valueText,
                  style: const TextStyle(color: AppTheme.goldAccent, fontWeight: FontWeight.bold, fontSize: 13),
                ),
              ],
            ),
            const SizedBox(height: 8),
            child,
          ],
        ),
      ),
    );
  }
}
