import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/property_provider.dart';
import '../theme/app_theme.dart';
import '../widgets/property_card.dart';
import '../widgets/filter_dialog.dart';
import 'add_property_screen.dart';
import 'property_detail_screen.dart';

class HomeScreen extends StatelessWidget {
  final VoidCallback onOpenCalculator;

  const HomeScreen({
    super.key,
    required this.onOpenCalculator,
  });

  void _showFilterSheet(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: AppTheme.darkSurface,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(24)),
      ),
      builder: (_) => const FilterDialog(),
    );
  }

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<PropertyProvider>();
    final properties = provider.properties;

    return Scaffold(
      appBar: AppBar(
        title: const Row(
          children: [
            Icon(Icons.real_estate_agent, color: AppTheme.goldAccent),
            SizedBox(width: 8),
            Text("عقاراتي", style: TextStyle(fontWeight: FontWeight.w900, letterSpacing: 0.5)),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.add_home_work, color: AppTheme.goldAccent),
            tooltip: "إضافة إعلان جديد",
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const AddPropertyScreen()),
              );
            },
          ),
        ],
      ),
      body: Column(
        children: [
          // شريط البحث وأيقونة التصفية
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 8, 16, 8),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    decoration: InputDecoration(
                      hintText: "ابحث بالمدينة أو الحي...",
                      prefixIcon: const Icon(Icons.search, color: AppTheme.goldAccent),
                      suffixIcon: provider.searchQuery.isNotEmpty
                          ? IconButton(
                              icon: const Icon(Icons.clear, size: 18),
                              onPressed: () => provider.setSearchQuery(''),
                            )
                          : null,
                      contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                    ),
                    onChanged: provider.setSearchQuery,
                  ),
                ),
                const SizedBox(width: 10),
                Material(
                  color: (provider.selectedCity != 'الكل' ||
                          provider.selectedPurpose != 'الكل' ||
                          provider.selectedType != 'الكل')
                      ? AppTheme.goldAccent
                      : AppTheme.darkSurfaceVariant,
                  borderRadius: BorderRadius.circular(14),
                  child: InkWell(
                    borderRadius: BorderRadius.circular(14),
                    onTap: () => _showFilterSheet(context),
                    child: Padding(
                      padding: const EdgeInsets.all(12),
                      child: Icon(
                        Icons.tune,
                        color: (provider.selectedCity != 'الكل' ||
                                provider.selectedPurpose != 'الكل' ||
                                provider.selectedType != 'الكل')
                            ? AppTheme.primaryOnColor
                            : AppTheme.textMain,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),

          // شرائح التصفية السريعة
          SizedBox(
            height: 48,
            child: ListView(
              scrollDirection: Axis.horizontal,
              padding: const EdgeInsets.symmetric(horizontal: 16),
              children: [
                _quickChip(context, "الكل", provider.selectedPurpose == 'الكل' && provider.selectedType == 'الكل', () {
                  provider.setPurpose('الكل');
                  provider.setType('الكل');
                }),
                _quickChip(context, "للبيع", provider.selectedPurpose == 'للبيع', () => provider.setPurpose('للبيع')),
                _quickChip(context, "للإيجار", provider.selectedPurpose == 'للإيجار', () => provider.setPurpose('للإيجار')),
                _quickChip(context, "فلل", provider.selectedType == 'فيلا', () => provider.setType('فيلا')),
                _quickChip(context, "شقق", provider.selectedType == 'شقة', () => provider.setType('شقة')),
                _quickChip(context, "أدوار", provider.selectedType == 'دور', () => provider.setType('دور')),
                _quickChip(context, "أراضي", provider.selectedType == 'أرض', () => provider.setType('أرض')),
              ],
            ),
          ),
          const SizedBox(height: 8),

          // عدد النتائج
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 18, vertical: 4),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  "العقارات المتاحة (${properties.length})",
                  style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13, color: AppTheme.textSecondary),
                ),
                if (provider.selectedCity != 'الكل' ||
                    provider.selectedPurpose != 'الكل' ||
                    provider.selectedType != 'الكل' ||
                    provider.searchQuery.isNotEmpty)
                  GestureDetector(
                    onTap: provider.resetFilters,
                    child: const Text(
                      "إلغاء التصفية",
                      style: TextStyle(color: AppTheme.goldAccent, fontSize: 12, fontWeight: FontWeight.bold),
                    ),
                  ),
              ],
            ),
          ),

          // قائمة العقارات
          Expanded(
            child: properties.isEmpty
                ? Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Icon(Icons.search_off, size: 56, color: AppTheme.textSecondary),
                        const SizedBox(height: 12),
                        const Text(
                          "لا توجد عقارات مطابقة لمعايير البحث",
                          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                        ),
                        const SizedBox(height: 8),
                        TextButton(
                          onPressed: provider.resetFilters,
                          child: const Text("إعادة ضبط البحث والتصفية", style: TextStyle(color: AppTheme.goldAccent)),
                        ),
                      ],
                    ),
                  )
                : ListView.builder(
                    padding: const EdgeInsets.fromLTRB(16, 8, 16, 80),
                    itemCount: properties.length,
                    itemBuilder: (context, index) {
                      final prop = properties[index];
                      return PropertyCard(
                        property: prop,
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (_) => PropertyDetailScreen(
                                property: prop,
                                onToggleFavorite: () => provider.toggleFavorite(prop.id),
                                onOpenCalculator: onOpenCalculator,
                              ),
                            ),
                          );
                        },
                        onToggleFavorite: () => provider.toggleFavorite(prop.id),
                      );
                    },
                  ),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        backgroundColor: AppTheme.goldAccent,
        foregroundColor: AppTheme.primaryOnColor,
        onPressed: () {
          Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const AddPropertyScreen()),
          );
        },
        icon: const Icon(Icons.add),
        label: const Text("أضف عقارك", style: TextStyle(fontWeight: FontWeight.bold)),
      ),
    );
  }

  Widget _quickChip(BuildContext context, String label, bool isSelected, VoidCallback onTap) {
    return Padding(
      padding: const EdgeInsets.only(left: 8),
      child: FilterChip(
        label: Text(label, style: const TextStyle(fontSize: 12)),
        selected: isSelected,
        selectedColor: AppTheme.goldAccent,
        backgroundColor: AppTheme.darkSurfaceVariant,
        labelStyle: TextStyle(
          color: isSelected ? AppTheme.primaryOnColor : AppTheme.textMain,
          fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
        ),
        side: const BorderSide(color: AppTheme.darkOutline),
        onSelected: (_) => onTap(),
      ),
    );
  }
}
