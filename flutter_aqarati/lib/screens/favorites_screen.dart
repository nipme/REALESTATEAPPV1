import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/property_provider.dart';
import '../theme/app_theme.dart';
import '../widgets/property_card.dart';
import 'property_detail_screen.dart';

class FavoritesScreen extends StatelessWidget {
  final VoidCallback onExploreTap;
  final VoidCallback onOpenCalculator;

  const FavoritesScreen({
    super.key,
    required this.onExploreTap,
    required this.onOpenCalculator,
  });

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<PropertyProvider>();
    final favorites = provider.favoriteProperties;

    return Scaffold(
      appBar: AppBar(
        title: Row(
          children: [
            const Icon(Icons.favorite, color: AppTheme.favoriteRed),
            const SizedBox(width: 8),
            Text("المفضلة (${favorites.length})"),
          ],
        ),
      ),
      body: favorites.isEmpty
          ? Center(
              child: Padding(
                padding: const EdgeInsets.all(32),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                      padding: const EdgeInsets.all(20),
                      decoration: BoxDecoration(
                        color: AppTheme.darkSurfaceVariant,
                        shape: BoxShape.circle,
                        border: Border.all(color: AppTheme.darkOutline),
                      ),
                      child: const Icon(Icons.favorite_border, size: 48, color: AppTheme.textSecondary),
                    ),
                    const SizedBox(height: 18),
                    const Text(
                      "قائمة المفضلة فارغة حالياً",
                      style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    const Text(
                      "اضغط على رمز القلب في أي عقار لإضافته إلى قائمة المفضلة والرجوع إليه لاحقاً بسهولة.",
                      textAlign: TextAlign.center,
                      style: TextStyle(color: AppTheme.textSecondary, height: 1.5),
                    ),
                    const SizedBox(height: 24),
                    ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppTheme.goldAccent,
                        foregroundColor: AppTheme.primaryOnColor,
                        padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      onPressed: onExploreTap,
                      child: const Text("تصفح العقارات الآن", style: TextStyle(fontWeight: FontWeight.bold)),
                    ),
                  ],
                ),
              ),
            )
          : ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: favorites.length,
              itemBuilder: (context, index) {
                final prop = favorites[index];
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
    );
  }
}
