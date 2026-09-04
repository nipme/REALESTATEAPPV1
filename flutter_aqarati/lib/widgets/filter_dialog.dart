import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/property_provider.dart';
import '../theme/app_theme.dart';

class FilterDialog extends StatelessWidget {
  const FilterDialog({super.key});

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<PropertyProvider>();
    final purposes = ['الكل', 'للبيع', 'للإيجار'];
    final types = ['الكل', 'فيلا', 'شقة', 'دور', 'أرض'];
    final cities = ['الكل', 'الرياض', 'جدة', 'الدمام', 'الخبر'];

    return Padding(
      padding: const EdgeInsets.all(20),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              const Row(
                children: [
                  Icon(Icons.tune, color: AppTheme.goldAccent),
                  SizedBox(width: 8),
                  Text("تصفية العقارات", style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                ],
              ),
              IconButton(
                icon: const Icon(Icons.close),
                onPressed: () => Navigator.pop(context),
              ),
            ],
          ),
          const SizedBox(height: 16),

          // نوع العرض
          const Text("نوع العرض", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
          const SizedBox(height: 6),
          Wrap(
            spacing: 8,
            children: purposes.map((p) {
              final isSel = provider.selectedPurpose == p;
              return ChoiceChip(
                label: Text(p),
                selected: isSel,
                selectedColor: AppTheme.goldAccent,
                labelStyle: TextStyle(
                  color: isSel ? AppTheme.primaryOnColor : AppTheme.textMain,
                  fontWeight: FontWeight.bold,
                ),
                onSelected: (_) => provider.setPurpose(p),
              );
            }).toList(),
          ),
          const SizedBox(height: 14),

          // المدينة
          const Text("المدينة", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
          const SizedBox(height: 6),
          Wrap(
            spacing: 8,
            children: cities.map((c) {
              final isSel = provider.selectedCity == c;
              return ChoiceChip(
                label: Text(c),
                selected: isSel,
                selectedColor: AppTheme.goldAccent,
                labelStyle: TextStyle(
                  color: isSel ? AppTheme.primaryOnColor : AppTheme.textMain,
                  fontWeight: FontWeight.bold,
                ),
                onSelected: (_) => provider.setCity(c),
              );
            }).toList(),
          ),
          const SizedBox(height: 14),

          // نوع العقار
          const Text("نوع العقار", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
          const SizedBox(height: 6),
          Wrap(
            spacing: 8,
            children: types.map((t) {
              final isSel = provider.selectedType == t;
              return ChoiceChip(
                label: Text(t),
                selected: isSel,
                selectedColor: AppTheme.goldAccent,
                labelStyle: TextStyle(
                  color: isSel ? AppTheme.primaryOnColor : AppTheme.textMain,
                  fontWeight: FontWeight.bold,
                ),
                onSelected: (_) => provider.setType(t),
              );
            }).toList(),
          ),
          const SizedBox(height: 24),

          // الأزرار
          Row(
            children: [
              Expanded(
                child: OutlinedButton(
                  style: OutlinedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 12),
                    side: const BorderSide(color: AppTheme.darkOutline),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  ),
                  onPressed: () {
                    provider.resetFilters();
                  },
                  child: const Text("إعادة ضبط", style: TextStyle(color: AppTheme.textMain)),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: AppTheme.goldAccent,
                    foregroundColor: AppTheme.primaryOnColor,
                    padding: const EdgeInsets.symmetric(vertical: 12),
                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                  ),
                  onPressed: () => Navigator.pop(context),
                  child: const Text("عرض النتائج", style: TextStyle(fontWeight: FontWeight.bold)),
                ),
              ),
            ],
          ),
          const SizedBox(height: 16),
        ],
      ),
    );
  }
}
